-- ============================================================
-- 유저 (실제 OAuth 계정 기준)
-- ============================================================
INSERT INTO users (id, provider, provider_user_id, email, nickname, profile_image_url, role, created_at, updated_at)
VALUES
  (1, 'NAVER',  '_EjAXcYGgkNm-D8S7kdCmybvd9-qe25AFFneyIW5sGI', 'soobin7999@naver.com',      '정수빈',              NULL,                                                                                                          'ADMIN', '2026-05-28 15:23:19', '2026-05-28 15:23:19'),
  (2, 'GOOGLE', '101980582012732483032',                         'soobin84786861@gmail.com',  '정수빈',              'https://lh3.googleusercontent.com/a/ACg8ocK6VckXlw0imm99B9AdBrCrcZV1CVXe9Jtho_YYO4E1WPSuQXf4=s96-c',   'USER',  '2026-05-28 16:27:09', '2026-05-28 16:27:09'),
  (3, 'KAKAO',  '4918147891',                                   NULL,                        '수빈이',              NULL,                                                                                                          'USER',  '2026-05-28 16:33:33', '2026-05-28 16:33:33');
-- (4, 'GOOGLE', '103849674859398310378',                         'soobin7999@g.skku.edu',     '빅데이터학과/정수빈', 'https://lh3.googleusercontent.com/a/ACg8ocKu6JisMStWVHKTItArtYPRotOb9-ixBmr1rdDTh2TcQm70lQ=s96-c',   'ADMIN', '2026-05-28 16:40:40', '2026-06-05 16:38:32');

-- ============================================================
-- 캠핑장 3곳
-- ============================================================
INSERT INTO camp_sites (name, link, region_code, district_code, check_in_time, check_out_time, created_at, updated_at)
VALUES
  ('별빛 오토캠핑장',    'https://naver.me/camp1', 'GANGWON',  'GANGWON_HONGCHEON_GUN', '14:00:00', '11:00:00', NOW(), NOW()),
  ('숲속의 아침 캠핑장', 'https://naver.me/camp2', 'GYEONGGI', 'GYEONGGI_GAPYEONG_GUN', '15:00:00', '12:00:00', NOW(), NOW()),
  ('제주 바람 캠핑장',   'https://naver.me/camp3', 'JEJU',     'JEJU_JEJU_SI',           '13:00:00', '11:00:00', NOW(), NOW());

