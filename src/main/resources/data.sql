insert into user values(1,'123', 'snezhi');
insert into user values(2, '123', 'tedi');
insert into user values(3,'123', 'milen');

insert into presentation values(1, 'Nature','art,cool', 1);
insert into presentation values(2, 'Technologies','cool,it', 2);
insert into presentation values(3, 'Art','art,fun', 3);
insert into presentation values(4, 'University','work,it',1);
insert into presentation values(5, 'Work','work,it',1);
insert into presentation values(6, 'Business','work,fun',1);
insert into presentation values(7, 'School','fun,cool',1);
insert into presentation values(8, 'Machine Learning','technology,it',1);
insert into presentation values(9, 'Artificial Intelligence','technology',1);
insert into presentation values(10, 'Social','social,cool',1);
insert into presentation values(11, 'Personal Skills','social',1);
insert into presentation values(12, 'Java World','java,technology',1);

insert into slide(id,presentation_id,index,qr) values(1,1,0,'1.png');
insert into slide(id,presentation_id,index,qr) values(2,1,1,'2.png');
insert into slide(id,presentation_id,index,qr) values(3,1,2,'3.png');
insert into slide(id,presentation_id,index,qr) values(4,1,3,'4.png');
insert into slide(id,presentation_id,index,qr) values(5,1,4,'5.png');