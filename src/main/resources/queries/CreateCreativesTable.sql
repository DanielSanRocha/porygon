CREATE TABLE IF NOT EXISTS `tb_creatives` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `id_advertiser` bigint(20) NOT NULL,
    `name` varchar(200) NOT NULL UNIQUE,
    `filename` varchar(200) NOT NULL UNIQUE,
    `description` text NOT NULL,
    `width` int NOT NULL,
    `height` int NOT NULL,
    `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_advertiser) REFERENCES tb_advertisers(id),
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8
