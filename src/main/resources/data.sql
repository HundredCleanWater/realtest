

INSERT INTO USER VALUES(1, 'test@kakao.com', 2008054770, 2, '서울역', 37.55612530289547, 126.97234632175427,'가나다 ','$2a$10$39fqGI9cnwzKJ7jRjzR8EeiBojoF4yRSnki1ZfxMjuJtAHAwvaDtO','http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_640x640.jpg','USER','백정수1');
INSERT INTO USER VALUES(2, 'test@kakao.com', 2008054771, 2, '아몰랑', 24.12,34.12,'가나다 ','$2a$10$39fqGI9cnwzKJ7jRjzR8EeiBojoF4yRSnki1ZfxMjuJtAHAwvaDtO','http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_640x640.jpg','USER','이한울1');
INSERT INTO USER VALUES(3,'test@kakao.com', 2008054772, 2, '아몰랑', 24.12,34.12,'가나다 ','$2a$10$39fqGI9cnwzKJ7jRjzR8EeiBojoF4yRSnki1ZfxMjuJtAHAwvaDtO','http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_640x640.jpg','USER','김주란1');

-- 2km이내
INSERT INTO RESTAURANT VALUES(1, NOW(), NOW(), TRUE, 'asd', '서울특별시 용산구 청파로93길 18-1', 126.96777168038808, 37.554566432261296, 3, '서울역 철도떡볶이', '분식', '밀떡', '허파만', 1, NULL);
INSERT INTO RESTAURANT VALUES(2, NOW(), NOW(), TRUE, 'asd', '남대문로5가 827번지 상가동 1층 109호 남산트라팰리스 중구 서울특별시 KR', 126.97562247441562, 37.554092939356934, 2, '공수간', '분식', '밀떡', '허파만', 1, NULL);
INSERT INTO RESTAURANT VALUES(3, NOW(), NOW(), TRUE, 'asd', '서울특별시 용산구 후암동 후암로 78', 126.97669192389391, 37.553157389989764, 4, '삼거리떡볶이', '분식', '밀떡', '허파만', 1, NULL);
INSERT INTO RESTAURANT VALUES(4, NOW(), NOW(), TRUE, 'asd', '서울특별시 용산구 후암동 439-10',  126.97668065499113, 37.55346147661965,1, '후암동 간판없는 떡볶이 집', '분식', '밀떡','허파만',1,NULL);
INSERT INTO RESTAURANT VALUES(5, NOW(), NOW(), TRUE, 'asd', '봉래동1가 104-1번지 2층 중구 서울특별시 KR', 126.9741889897714, 37.55973325358363, 5, '써니즉석떡볶이', '분식', '밀떡','허파만',1,NULL);
INSERT INTO RESTAURANT VALUES(6, NOW(), NOW(), TRUE, 'asd', '후암동 56-22번지 지상1층 용산구 서울특별시 KR', 126.977870931507, 37.550615387042455, 2, '달콤한떡볶이', '분식', '밀떡','허파만',1,NULL);
INSERT INTO RESTAURANT VALUES(7, NOW(), NOW(), TRUE, 'asd', '142, 충정로3가 서대문구 서울특별시',126.96349204912416, 37.563021913820904, 2, '철길떡볶이', '분식', '밀떡','허파만',1,NULL);
INSERT INTO RESTAURANT VALUES(8, NOW(), NOW(), TRUE, 'asd', '서울특별시 용산구 한강로2가 305-1',  126.9695228824581, 37.53255510612051, 3, '현선이네 용산본점', '분식', '밀떡','허파만',1,NULL);


INSERT INTO RESTAURANT_LIKES (RESTAURANT_ID, USER_ID) VALUES (3, 1);
INSERT INTO RESTAURANT_LIKES (RESTAURANT_ID, USER_ID) VALUES (3, 2);
INSERT INTO RESTAURANT_LIKES (RESTAURANT_ID, USER_ID) VALUES (3, 3);
INSERT INTO RESTAURANT_LIKES (RESTAURANT_ID, USER_ID) VALUES (2, 1);
INSERT INTO RESTAURANT_LIKES (RESTAURANT_ID, USER_ID) VALUES (2, 2);
INSERT INTO RESTAURANT_LIKES (RESTAURANT_ID, USER_ID) VALUES (1, 1);

INSERT INTO REVIEW VALUES(1,now(),now(),'맛있엉요!','default.png','맛있는집',0,1,1,1,NULL);
INSERT INTO REVIEW VALUES(2,now(),now(),'맛있엉요!','default.png','맛있는집',0,1,2,1,NULL);
INSERT INTO REVIEW VALUES(3,now(),now(),'맛있엉요!','default.png','맛있는집',0,1,3,1,NULL);

-- meeting
INSERT INTO MEETING VALUES (1,now(),now(),'되나?',now()+3,3,'이마트둔산점',36.357613757360426,127.37760267571373,now()+4,'타이틀',2,1,now()+2,1,1);



