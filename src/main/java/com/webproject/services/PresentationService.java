package com.webproject.services;

import com.webproject.form.PresentationForm;
import com.webproject.models.Presentation;
import com.webproject.models.Slide;
import com.webproject.repositories.PresentationRepository;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;

import com.webproject.repositories.SlideRepository;
import com.webproject.repositories.UserRepository;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.zeroturnaround.zip.ZipUtil;

@Service
public class PresentationService {

    private static final int RESULTS_PER_PAGE = 10;

    @Autowired
    PresentationRepository presentationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SlideRepository slideRepository;

    public Page<Presentation> findAll(int pageIndex, String tag) {
        if (tag != null) {
            return findAllForTag(tag, pageIndex);
        } else {
            return findAll(pageIndex);
        }
    }

    public Optional<Presentation> getPresentationById(Long id) { 	
    	return presentationRepository.findById(id);
    }
    
    public Page<Presentation> findAll(int pageIndex) {
        return presentationRepository.findAll(PageRequest.of(pageIndex, RESULTS_PER_PAGE));
    }

    public Page<Presentation> findAllForTag(String tag, int pageIndex) {
        return presentationRepository.findByTagsContaining(tag, PageRequest.of(pageIndex, RESULTS_PER_PAGE));
    }

    public Page<Presentation> findAllForUser(String userId, int pageIndex) {
        //all for user id
        return presentationRepository.findAll(PageRequest.of(pageIndex, RESULTS_PER_PAGE));
    }

    public Set<String> getAllDistinctTags() {
        Set<String> tags = new HashSet<>();
        List<Presentation> presentations = presentationRepository.findAll();
        presentations.stream().forEach(p -> tags.addAll(Arrays.asList(p.getTags().split(","))));
        return tags;
    }

    public void exportAll() {
        ZipUtil
                .pack(new File("src/main/resources/static/files"), new File("src/main/resources/static/presentations.zip"));
    }

    public File decompressZipToDestination(MultipartFile zipFile) throws IOException, ZipException {
        File destDir = createTempDirectory();
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(zipFile.getInputStream());
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            File newFile = newFile(destDir, zipEntry);
            FileOutputStream fos = new FileOutputStream(newFile);
            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
        return destDir;
    }

    private static File createTempDirectory() throws IOException {
        return Files.createTempDirectory("temp_folder").toFile();
    }

    private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    public List<Presentation> createPresentationsFrom(File destination, PresentationForm input, Long userId)
            throws IOException, StringIndexOutOfBoundsException, NumberFormatException {
        //for each file make a presentation
        File[] listOfFiles = destination.listFiles();
        List<Presentation> presentationsToSave = new ArrayList<>();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                System.out.println("File name is " + file.getName());
                // presentation for repository
                Presentation p = Presentation.builder()
                        .name(input.getName())
                        .tags(input.getTags())
                        .user(userRepository.getOne(userId)).build();
                List<Slide> slidesToSave = new ArrayList<>();
                //create directory for presentation with name
                File presentationFolder = new File("src/main/resources/static/img/presentations",p.getName());
                if (!presentationFolder.exists()) {
                    Files.createDirectory(presentationFolder.toPath());
                }
                //create slide directory
                String destSlides = "slides";
                File slidesDir = new File(presentationFolder,destSlides); //dest here is the name of the folder to save all the slides
                if (!slidesDir.exists()) {
                    Files.createDirectory(slidesDir.toPath());
                }
                //String destImages = "slides" + File.separator + input.getZipFile().getOriginalFilename();
                //File outFile = new File(destImages);
                //System.out.println("Destination is " + destSlides);
                //if (!outFile.exists()) {
                //    Files.createDirectory(outFile.toPath());
                //}
                FileInputStream inputStream = new FileInputStream(file);
                XMLSlideShow ppt = new XMLSlideShow(inputStream);
                //getting the dimensions and size of the slide
                Dimension pageSize = ppt.getPageSize();
                List<XSLFSlide> slide = ppt.getSlides();
                //for all slides create images
                createPngFromSlides(slide, new BufferedImage(pageSize.width, pageSize.height, BufferedImage.TYPE_INT_RGB), pageSize, slidesDir);
                if (slidesDir.exists() && slidesDir.isDirectory()) {
                    final FilenameFilter IMAGE_FILTER = (dir, name) -> name.endsWith(".png"); //make sure only png are valid
                    for (final File f : slidesDir.listFiles(IMAGE_FILTER)) {        	
                        byte[] slideBytes = Files.readAllBytes(f.toPath());
                        int slideStringIndex = f.getName().lastIndexOf("slide") + 5;
                        int slideIndex = Integer.valueOf(f.getName().substring(slideStringIndex, f.getName().indexOf(".png")));
                        slidesToSave.add(Slide.builder().index(slideIndex).image(slideBytes).presentation(p).build());
                    }
                    p.setSlides(slidesToSave);
                    p = presentationRepository.save(p);
                    slideRepository.saveAll(slidesToSave);
                    presentationsToSave.add(p);
                }
            }
        }
        return presentationsToSave;
    }

    private void createPngFromSlides(List<XSLFSlide> slide, BufferedImage img, Dimension pageSize, File outFile) throws IOException {
        for (int i = 0; i < slide.size(); i++) {
            Graphics2D graphics = img.createGraphics();
            //clear the drawing area
            graphics.setPaint(Color.white);
            graphics.fill(new Rectangle2D.Float(0, 0, pageSize.width, pageSize.height));
            //render
            slide.get(i).draw(graphics);
            //creating an image file as output
            if (!outFile.exists()) {
                outFile.mkdir();
            }
            String slideDestination = outFile.getCanonicalPath() + File.separator + "slide" + i + ".png"; // slide name in directory
            System.out.println("Creating " + slideDestination);
            OutputStream out = new FileOutputStream(slideDestination);
            javax.imageio.ImageIO.write(img, "png", out);
            out.close();
        }
    }
    
	public File uploadPresentation(MultipartFile zipFile) throws IOException {
        byte[] buffer = new byte[1024];
		InputStream fis = zipFile.getInputStream();
		File destDir = createTempDirectory();
		File newFile = new File(destDir, zipFile.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(newFile);
		int len;
		while ((len = fis.read(buffer)) > 0) {
			fos.write(buffer, 0, len);
		}
		fos.close();
		fis.close();
		return newFile.getParentFile();
	}

}
