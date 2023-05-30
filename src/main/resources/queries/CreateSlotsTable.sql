CREATE TABLE IF NOT EXISTS `tb_slots` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `name` varchar(200) NOT NULL UNIQUE,
    `description` text NOT NULL,
    `width` int NOT NULL,
    `height` int NOT NULL,
    `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8
