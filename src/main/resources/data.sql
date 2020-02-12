insert into user values(1,'123', 'snezhi');
insert into user values(2, '123', 'tedi');
insert into user values(3,'123', 'milen');

insert into presentation values(1, 'Работна презентация','работа,презентация,проект', 1);
insert into presentation values(2, 'Уеб Технологии','работа,презентация,проект,забавно', 2);


insert into slide(id,presentation_id,index) values(1,1,0);
insert into slide(id,presentation_id,index) values(2,1,1);
insert into slide(id,presentation_id,index) values(3,1,2);
insert into slide(id,presentation_id,index) values(4,1,3);
insert into slide(id,presentation_id,index) values(5,1,4);

insert into slide(id,presentation_id,index) values(6,2,0);
insert into slide(id,presentation_id,index) values(7,2,1);
insert into slide(id,presentation_id,index) values(8,2,2);
insert into slide(id,presentation_id,index) values(9,2,3);
insert into slide(id,presentation_id,index) values(10,2,4);