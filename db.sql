create table `individuals` (
  `id` varchar(20) not null,
  `name` varchar(100) not null,
  `gender` varchar(5),
  `birthday` date,
  `alive` boolean,
  `death` date,

  primary key (`id`)
);


create table `families` (
  `id` varchar(20) not null,
  `married` date,
  `divorced` date,
  `husband_id` varchar(20),
  `wife_id` varchar(20),

  primary key (`id`),
  foreign key (`husband_id`) references individuals(`id`) on delete cascade,
  foreign key (`wife_id`) references individuals(`id`) on delete cascade
);