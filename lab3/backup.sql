PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;
CREATE TABLE User (
    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
    username VARCHAR(30) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password TEXT NOT NULL,
    register_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    description VARCHAR(300),
    profile_picture TEXT,
    country VARCHAR(50),
    age INTEGER NOT NULL,
    gender TEXT NOT NULL CHECK(gender IN ('male', 'female', 'other')),
    role TEXT DEFAULT 'user' CHECK(role IN ('admin', 'user'))
);
INSERT INTO User VALUES(1,'admin','admin@epaw.upf.edu','AdminAdmin1','2026-06-15 12:28:36','Platform administrator. Responsible for content moderation and user management.',NULL,'United States',35,'male','admin');
INSERT INTO User VALUES(2,'mireia_sole','mireia.sole@upf.edu','Password1','2026-06-15 12:28:36','EPAW student exploring social interfaces and group collaboration tools.','assets/users/mireia_sole.jpg','Spain',24,'female','user');
INSERT INTO User VALUES(3,'marti_ferrer','marti.ferrer@upf.edu','Password1','2026-06-15 12:28:36','Web Engineering student. I like clean APIs and accessible front-ends.','assets/users/marti_ferrer.jpg','Spain',22,'male','user');
INSERT INTO User VALUES(4,'laia_maspons','laia.maspons@upf.edu','Password1','2026-06-15 12:28:36','Master''s student at UPF. Coffee, code and castells on weekends.','assets/users/laia_maspons.jpg','Spain',23,'female','user');
INSERT INTO User VALUES(5,'arnau_sala','arnau.sala@upf.edu','Password1','2026-06-15 12:28:36','Backend enthusiast from Barcelona. Always up for a hackathon.','assets/users/arnau_sala.jpg','Andorra',21,'male','user');
INSERT INTO User VALUES(6,'joan_prat','joan.prat@upf.edu','Password1','2026-06-15 12:28:36','Full-stack developer in training. Montserrat trails and side projects.','assets/users/joan_prat.jpg','Spain',25,'male','user');
INSERT INTO User VALUES(7,'carla_roca','carla.roca@upf.edu','Password1','2026-06-15 12:28:36','UX-minded engineer. I sketch wireframes before I write servlets.','assets/users/carla_roca.jpg','France',20,'female','user');
INSERT INTO User VALUES(8,'pol_vidal','pol.vidal@upf.edu','Password1','2026-06-15 12:28:36','Lab 3 survivor. I refactor for fun and document for others.','assets/users/pol_vidal.jpg','Spain',27,'male','user');
INSERT INTO User VALUES(9,'anna_grau','anna.grau@upf.edu','Password1','2026-06-15 12:28:36','First-year EPAW student from Girona. Learning Java and Catalan typography.','assets/users/anna_grau.jpg','Spain',19,'female','user');
INSERT INTO User VALUES(10,'oscar_puig','oscar.puig@upf.edu','Password1','2026-06-15 12:28:36','Data curious developer. I enjoy timelines and recommendation ideas.','assets/users/oscar_puig.jpg','Portugal',26,'male','user');
INSERT INTO User VALUES(11,'silvia_marti','silvia.marti@upf.edu','Password1','2026-06-15 12:28:36','Frontend lover. Fraunces font evangelist and terracotta palette fan.','assets/users/silvia_marti.jpg','Spain',23,'female','user');
INSERT INTO User VALUES(12,'gerard_sanz','gerard.sanz@upf.edu','Password1','2026-06-15 12:28:36','Computer engineering at UPF. Basketball after class.','assets/users/gerard_sanz.jpg','Belgium',22,'male','user');
INSERT INTO User VALUES(13,'nuria_torrent','nuria.torrent@upf.edu','Password1','2026-06-15 12:28:36','Interested in AI ethics and practical machine-learning prototypes.','assets/users/nuria_torrent.jpg','Spain',24,'female','user');
INSERT INTO User VALUES(14,'albert_miro','albert.miro@upf.edu','Password1','2026-06-15 12:28:36','DevOps-curious student. Docker, Maven and strong coffee.','assets/users/albert_miro.jpg','Italy',28,'male','user');
INSERT INTO User VALUES(15,'mar_clotet','mar.clotet@upf.edu','Password1','2026-06-15 12:28:36','Non-binary dev from Lleida. Inclusive design matters.','assets/users/mar_clotet.jpg','Spain',21,'other','user');
INSERT INTO User VALUES(16,'jordi_bosch','jordi.bosch@upf.edu','Password1','2026-06-15 12:28:36','Campus events volunteer and part-time photographer.','assets/users/jordi_bosch.jpg','Germany',30,'male','user');
INSERT INTO User VALUES(17,'montserrat_vila','montserrat.vila@upf.edu','Password1','2026-06-15 12:28:36','Project manager apprentice. I keep group chats organised.','assets/users/montserrat_vila.jpg','Spain',29,'female','user');
INSERT INTO User VALUES(18,'xavier_campos','xavier.campos@upf.edu','Password1','2026-06-15 12:28:36','Erasmus at UPF from Amsterdam. Bikes and REST APIs.','assets/users/xavier_campos.jpg','Netherlands',23,'male','user');
INSERT INTO User VALUES(19,'elena_font','elena.font@upf.edu','Password1','2026-06-15 12:28:36','Software engineering student. Open source contributor when exams allow.','assets/users/elena_font.jpg','Spain',22,'female','user');
INSERT INTO User VALUES(20,'sergi_duran','sergi.duran@upf.edu','Password1','2026-06-15 12:28:36','Mobile-first thinker learning server-side patterns this semester.','assets/users/sergi_duran.jpg','Spain',24,'male','user');
INSERT INTO User VALUES(21,'iris_pons','iris.pons@upf.edu','Password1','2026-06-15 12:28:36','Design and engineering double interest. Figma by day, JSP by night.','assets/users/iris_pons.jpg','United Kingdom',20,'female','user');
INSERT INTO User VALUES(22,'marc_trias','marc.trias@upf.edu','Password1','2026-06-15 12:28:36','Tarragona-born developer. Paella debates in issue threads.','assets/users/marc_trias.jpg','Spain',25,'male','user');
INSERT INTO User VALUES(23,'judit_solsona','judit.solsona@upf.edu','Password1','2026-06-15 12:28:36','AI lab assistant. Python notebooks and study groups.','assets/users/judit_solsona.jpg','Sweden',23,'female','user');
INSERT INTO User VALUES(24,'pau_benet','pau.benet@upf.edu','Password1','2026-06-15 12:28:36','Junior developer intern. Learning from code reviews.','assets/users/pau_benet.jpg','Spain',21,'male','user');
INSERT INTO User VALUES(25,'carme_rovira','carme.rovira@upf.edu','Password1','2026-06-15 12:28:36','Graduate student researching community platforms.','assets/users/carme_rovira.jpg','Spain',26,'female','user');
INSERT INTO User VALUES(26,'quim_serra','quim.serra@upf.edu','Password1','2026-06-15 12:28:36','Weekend climber. I push small fixes and write clear commit messages.','assets/users/quim_serra.jpg','Switzerland',22,'male','user');
INSERT INTO User VALUES(27,'Jaume','Jaume@j','Jordi201','2026-06-15 12:29:42','Anashe','Jaume.png','Czechia',23,'male','admin');
INSERT INTO User VALUES(28,'Marti','marti@gmail.com','Jordi201','2026-06-15 14:17:46','',NULL,'',23,'male','user');
INSERT INTO User VALUES(29,'Marta','jhdsf@djg','Jordi201','2026-06-15 14:19:44','','Marta.png','',22,'male','user');
CREATE TABLE IF NOT EXISTS "Group" (
    group_id INTEGER PRIMARY KEY AUTOINCREMENT,
    group_name VARCHAR(50) NOT NULL,
    description VARCHAR(300),
    group_picture TEXT,
    date_of_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    creator_id INTEGER,
    participants INTEGER,
    owner VARCHAR(30),
    privacy TEXT CHECK(privacy IN ('public', 'private')),
    FOREIGN KEY (creator_id) REFERENCES User(user_id) ON DELETE SET NULL
);
INSERT INTO "Group" VALUES(1,'Campus Events','Announcements and meetups around the Pompeu Fabra campus.','assets/groups/campus.png','2026-06-15 12:28:36',1,13,'admin','private');
INSERT INTO "Group" VALUES(2,'Web Engineering','Course projects, lab discussions and resources for the Web Engineering subject.','assets/groups/web.png','2026-06-15 12:28:36',1,7,'admin','public');
INSERT INTO "Group" VALUES(4,'Tech & Coding','Tips, tools and debate around software development and computer science. Open to everyone.',NULL,'2026-06-15 12:28:36',1,16,'admin','public');
INSERT INTO "Group" VALUES(5,'Student Life','Everything from campus hacks to exam stress and city life in Barcelona.',NULL,'2026-06-15 12:28:36',1,12,'admin','public');
INSERT INTO "Group" VALUES(7,'Global Tech','Public space for high-signal engineering ideas and trending technical discussions.','assets/groups/globalTech.png','2026-06-15 12:28:36',1,10,'admin','public');
INSERT INTO "Group" VALUES(8,'ANashe','aaa','group_8.png',1781527568348,27,1,'Jaume','public');
CREATE TABLE Post (
    post_id INTEGER PRIMARY KEY AUTOINCREMENT,
    response_id INTEGER DEFAULT NULL,
    content TEXT NOT NULL,
    post_picture TEXT,
    date_of_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    votes INTEGER DEFAULT 0,
    upvotes INTEGER DEFAULT 0,
    downvotes INTEGER DEFAULT 0,
    comment_count INTEGER DEFAULT 0,
    edited INTEGER DEFAULT 0,
    user_id INTEGER NOT NULL,
    group_id INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE,
    FOREIGN KEY (group_id) REFERENCES "Group"(group_id) ON DELETE CASCADE
);
INSERT INTO Post VALUES(5,NULL,'Lab 3 servlet wiring tips: remember to set user attributes before forwarding to the JSP.',NULL,'2026-06-10 08:20:00',6,6,0,2,0,3,2);
INSERT INTO Post VALUES(6,NULL,'Accessible forms checklist for our project UI ÔÇö green borders only on required fields.',NULL,'2026-06-09 13:10:00',4,5,1,1,0,4,2);
INSERT INTO Post VALUES(7,NULL,'JUnit tests for PostService are passing. I will open a PR tonight.',NULL,'2026-06-08 18:00:00',-2,1,3,0,0,8,2);
INSERT INTO Post VALUES(8,NULL,'Saturday outing confirmed: meet at 8:00 at the Montserrat cable car. Bring binoculars!','assets/posts/montserrat.jpg','2026-06-10 07:30:00',11,12,1,2,0,6,1);
INSERT INTO Post VALUES(9,NULL,'Photo walk next Thursday at the Forum. Open to all skill levels.',NULL,'2026-06-09 10:00:00',5,5,0,1,0,16,1);
INSERT INTO Post VALUES(10,NULL,'Reminder: guest lecture on urban planning this Friday at 16:00 in room 52.001.',NULL,'2026-06-07 15:20:00',2,2,0,2,0,17,1);
INSERT INTO Post VALUES(11,NULL,'Hot take: learning SQL properly is more valuable than knowing the latest JS framework. Change my mind.',NULL,'2026-06-10 10:00:00',17,18,1,0,0,8,4);
INSERT INTO Post VALUES(12,NULL,'Just discovered that adding an index to a single column cut our query time from 4s to 40ms. Never skip database indexes.',NULL,'2026-06-09 16:30:00',14,14,0,1,0,5,4);
INSERT INTO Post VALUES(13,NULL,'Free resources to learn system design that I wish I had found earlier: Designing Data-Intensive Applications + the ByteByteGo newsletter. Both free online.',NULL,'2026-06-08 09:00:00',9,10,1,1,0,10,4);
INSERT INTO Post VALUES(14,NULL,'Reminder: commit messages are documentation. "fix stuff" is not a commit message.',NULL,'2026-06-07 14:00:00',7,7,0,1,0,3,4);
INSERT INTO Post VALUES(15,NULL,'Docker tip: add a .dockerignore file before building your image. Went from a 1.2GB image to 180MB by ignoring node_modules and target/.',NULL,'2026-06-06 11:45:00',2,4,2,0,0,14,4);
INSERT INTO Post VALUES(16,NULL,'The best debugging skill is learning to read stack traces top to bottom without panicking. It took me two years to stop scrolling straight to the bottom.',NULL,'2026-06-05 17:20:00',2,2,0,2,0,4,4);
INSERT INTO Post VALUES(17,NULL,'Unpopular opinion: the library at 7am before everyone arrives is the best study spot on campus. Empty, silent, perfect.',NULL,'2026-06-10 07:15:00',16,16,0,1,0,9,5);
INSERT INTO Post VALUES(18,NULL,'Passed the algorithms exam by making hand-drawn diagrams instead of re-reading slides. Visual notes are underrated.',NULL,'2026-06-09 20:00:00',9,10,1,1,0,7,5);
INSERT INTO Post VALUES(19,NULL,'Hostal Gat Raval has decent beds and is walkable from UPF. Good option if friends visit for a weekend and hotels are too expensive.',NULL,'2026-06-08 12:30:00',3,3,0,1,0,18,5);
INSERT INTO Post VALUES(20,NULL,'If you are doing a final project demo, test it on the projector 30 minutes before. The cable situation in room 40.007 is a nightmare.',NULL,'2026-06-06 10:00:00',2,5,3,1,0,21,5);
INSERT INTO Post VALUES(21,NULL,'The rooftop terrace on the Ciutadella campus is open until 22:00 during exam season. Best kept secret for group study.',NULL,'2026-06-10 09:00:00',12,12,0,1,0,16,1);
INSERT INTO Post VALUES(22,NULL,'Accessibility tip for our lab projects: every form input needs an associated label element, not just a placeholder. Screen readers cannot read placeholders.',NULL,'2026-06-09 11:30:00',6,6,0,1,0,11,2);
INSERT INTO Post VALUES(23,NULL,'Barcelona dev tip: if your local environment breaks every Monday, script your full setup and run it from scratch once a week.',NULL,'2026-06-11 08:40:00',20,21,1,2,0,26,4);
INSERT INTO Post VALUES(24,NULL,'Thread: 7 mistakes we made in our first servlet app and how we fixed each one. #1: not validating ownership in edit/delete actions.',NULL,'2026-06-11 10:15:00',17,19,2,2,0,19,4);
INSERT INTO Post VALUES(25,NULL,'Public speaking hack for project demos: do one dry run with a 5-minute timer and one with a 3-minute timer. You will sound much sharper.',NULL,'2026-06-11 12:05:00',15,15,0,1,0,25,5);
INSERT INTO Post VALUES(26,NULL,'UPF exam-season map: these are the least crowded study corners by time slot. 8-10am library north wing is undefeated.',NULL,'2026-06-11 13:20:00',7,8,1,1,0,15,5);
INSERT INTO Post VALUES(27,NULL,'PSA: stop forcing microservices into student projects. A clean monolith with tests beats distributed chaos every single time.',NULL,'2026-06-11 14:45:00',14,17,3,1,0,20,4);
INSERT INTO Post VALUES(28,NULL,'Campus running club opened a beginner 3km loop on Tuesdays at 19:00. Friendly pace and no registration needed.',NULL,'2026-06-11 17:05:00',6,6,0,1,0,17,1);
INSERT INTO Post VALUES(29,NULL,'Cheat sheet idea: one page with SQL JOIN patterns (inner/left/self) solved 80% of our database lab issues.',NULL,'2026-06-11 18:10:00',11,11,0,1,0,24,2);
INSERT INTO Post VALUES(30,NULL,'Simple productivity stack for finals: 45/10 pomodoro blocks, notifications off, and one checklist for the day. Nothing fancy, works great.',NULL,'2026-06-11 19:30:00',4,4,0,1,0,9,5);
INSERT INTO Post VALUES(31,NULL,'Opinion: code reviews should include one positive note and one risk note. Teams improve faster when both are explicit.',NULL,'2026-06-11 20:15:00',12,13,1,1,0,6,4);
INSERT INTO Post VALUES(32,NULL,'Low-cost student meal list around Ciutadella (under 8 euro) with distance/time from campus. Added vegan options too.',NULL,'2026-06-11 21:00:00',-2,1,3,1,0,21,5);
INSERT INTO Post VALUES(69,NULL,'What changed my debugging speed the most: writing down assumptions before touching code. Most bugs were wrong assumptions, not syntax.',NULL,'2026-06-13 09:10:00',11,12,1,2,0,15,7);
INSERT INTO Post VALUES(70,NULL,'Do not optimize architecture in week 1. Build the happy path first, then profile. Premature abstractions are expensive.',NULL,'2026-06-13 08:45:00',9,9,0,2,0,16,7);
INSERT INTO Post VALUES(71,NULL,'A tiny SQL index can outperform a week of backend micro-optimizations. Start with the query plan, always.',NULL,'2026-06-13 08:10:00',10,11,1,2,0,17,7);
INSERT INTO Post VALUES(72,NULL,'If your PR description does not explain why, reviewers will guess. Explain intent, trade-offs and rollout.',NULL,'2026-06-13 07:40:00',5,5,0,2,0,18,7);
INSERT INTO Post VALUES(73,NULL,'Accessible UI check: labels, focus states, keyboard path, contrast. Four checks avoid most regressions.',NULL,'2026-06-13 07:05:00',7,7,0,2,0,19,7);
INSERT INTO Post VALUES(74,NULL,'Team productivity improved when we made a strict definition of done: tests, docs note and rollback plan.',NULL,'2026-06-13 06:30:00',1,3,2,3,0,20,7);
INSERT INTO Post VALUES(75,NULL,'Small monolith + clear boundaries beats accidental microservices for most student and early-stage projects.',NULL,'2026-06-12 18:20:00',10,10,0,2,0,21,7);
INSERT INTO Post VALUES(76,NULL,'Write postmortems for non-failures too. Capturing what went right is reusable engineering knowledge.',NULL,'2026-06-12 16:50:00',1,2,1,2,0,22,7);
INSERT INTO Post VALUES(77,NULL,'Before shipping any feature, test with a cold browser cache and a slow network preset. Reality check matters.',NULL,'2026-06-12 15:30:00',4,4,0,2,0,23,7);
INSERT INTO Post VALUES(78,NULL,'Fastest way to learn codebase ownership: rotate incident response and share concise repair notes after each fix.',NULL,'2026-06-12 14:10:00',1,1,0,2,0,24,7);
INSERT INTO Post VALUES(79,69,'Totally agree. Writing assumptions first also makes pair debugging much faster.',NULL,'2026-06-13 09:20:00',0,0,0,0,0,2,7);
INSERT INTO Post VALUES(80,69,'We started doing this in our lab and the number of random fixes dropped a lot.',NULL,'2026-06-13 09:25:00',0,0,0,0,0,3,7);
INSERT INTO Post VALUES(81,70,'This is the advice people skip and then they rewrite everything in week 3.',NULL,'2026-06-13 08:55:00',0,0,0,0,0,4,7);
INSERT INTO Post VALUES(82,70,'Happy path first saved us in EPAW, we had a demo before polishing edge cases.',NULL,'2026-06-13 09:00:00',0,0,0,0,0,6,7);
INSERT INTO Post VALUES(83,71,'Query plans should be taught earlier. They explain half the backend mysteries.',NULL,'2026-06-13 08:20:00',0,0,0,0,0,7,7);
INSERT INTO Post VALUES(84,71,'Indexing was literally the biggest performance win in our project too.',NULL,'2026-06-13 08:26:00',0,0,0,0,0,8,7);
INSERT INTO Post VALUES(85,72,'Intent section in PR templates should be mandatory.',NULL,'2026-06-13 07:50:00',0,0,0,0,0,9,7);
INSERT INTO Post VALUES(86,72,'Review time drops a lot when trade-offs are explicit. Great point.',NULL,'2026-06-13 07:54:00',0,0,0,0,0,10,7);
INSERT INTO Post VALUES(87,73,'These four checks are basically our release checklist now.',NULL,'2026-06-13 07:14:00',0,0,0,0,0,11,7);
INSERT INTO Post VALUES(88,73,'Keyboard path catches many issues that visual QA misses.',NULL,'2026-06-13 07:18:00',0,0,0,0,0,12,7);
INSERT INTO Post VALUES(90,74,'Rollback plan in student projects sounds overkill until the first bad deploy.',NULL,'2026-06-13 06:42:00',0,0,0,0,0,14,7);
INSERT INTO Post VALUES(91,75,'Exactly. Distribution adds operational load way too early.',NULL,'2026-06-12 18:28:00',0,0,0,0,0,25,7);
INSERT INTO Post VALUES(92,75,'Boundary-first monolith gave us clean code without infra pain.',NULL,'2026-06-12 18:34:00',0,0,0,0,0,26,7);
INSERT INTO Post VALUES(93,76,'Great point, wins should be documented too or we lose good patterns.',NULL,'2026-06-12 16:58:00',0,0,0,0,0,2,7);
INSERT INTO Post VALUES(94,76,'Postmortems for successes are underrated team memory.',NULL,'2026-06-12 17:03:00',0,0,0,0,0,3,7);
INSERT INTO Post VALUES(95,77,'Slow network preset exposed two race conditions in our app.',NULL,'2026-06-12 15:38:00',0,0,0,0,0,4,7);
INSERT INTO Post VALUES(96,77,'Cold-cache tests should be in every acceptance checklist.',NULL,'2026-06-12 15:43:00',0,0,0,0,0,6,7);
INSERT INTO Post VALUES(97,78,'Incident notes became onboarding gold in our student team.',NULL,'2026-06-12 14:20:00',0,0,0,0,0,7,7);
INSERT INTO Post VALUES(98,78,'This habit also improves handover quality when people rotate.',NULL,'2026-06-12 14:24:00',0,0,0,0,0,8,7);
INSERT INTO Post VALUES(120,5,'This tip saved me during the servlet wiring exercise.',NULL,'2026-06-13 10:05:00',0,0,0,0,0,2,2);
INSERT INTO Post VALUES(121,5,'Forwarding with request attributes is the part I always forget.',NULL,'2026-06-13 10:09:00',0,0,0,0,0,4,2);
INSERT INTO Post VALUES(122,6,'Good reminder. Placeholders are not a substitute for labels.',NULL,'2026-06-13 10:12:00',0,0,0,0,0,11,2);
INSERT INTO Post VALUES(123,8,'I am joining this outing, the timing works perfectly.',NULL,'2026-06-13 10:15:00',0,0,0,0,0,9,1);
INSERT INTO Post VALUES(124,8,'I can bring the camera and share photos afterwards.',NULL,'2026-06-13 10:18:00',0,0,0,0,0,16,1);
INSERT INTO Post VALUES(125,9,'Photo walk sounds great. Is it beginner friendly?',NULL,'2026-06-13 10:21:00',0,0,0,0,0,21,1);
INSERT INTO Post VALUES(126,10,'SQL fundamentals really pay off once the app grows.',NULL,'2026-06-13 10:24:00',0,0,0,0,0,5,1);
INSERT INTO Post VALUES(127,10,'Agreed. Bad queries are harder to hide than bad CSS.',NULL,'2026-06-13 10:27:00',0,0,0,0,0,8,1);
INSERT INTO Post VALUES(128,12,'Indexes feel like magic until you inspect the query plan.',NULL,'2026-06-13 10:30:00',0,0,0,0,0,14,4);
INSERT INTO Post VALUES(129,14,'This should be printed above every commit button.',NULL,'2026-06-13 10:33:00',0,0,0,0,0,3,4);
INSERT INTO Post VALUES(130,17,'Early library sessions are painfully effective.',NULL,'2026-06-13 10:36:00',0,0,0,0,0,7,5);
INSERT INTO Post VALUES(131,18,'Visual notes helped me a lot with dynamic programming too.',NULL,'2026-06-13 10:39:00',0,0,0,0,0,15,5);
INSERT INTO Post VALUES(132,20,'Testing on the projector is not optional after last week.',NULL,'2026-06-13 10:42:00',0,0,0,0,0,19,5);
INSERT INTO Post VALUES(133,21,'Rooftop study sessions are now officially on my schedule.',NULL,'2026-06-13 10:45:00',0,0,0,0,0,17,1);
INSERT INTO Post VALUES(135,23,'A setup script would have saved my Monday morning.',NULL,'2026-06-13 10:51:00',0,0,0,0,0,26,4);
INSERT INTO Post VALUES(136,23,'Cold-starting the project is the best deployment test.',NULL,'2026-06-13 10:54:00',0,0,0,0,0,24,4);
INSERT INTO Post VALUES(137,24,'Ownership checks are exactly what our first prototype missed.',NULL,'2026-06-13 10:57:00',0,0,0,0,0,20,4);
INSERT INTO Post VALUES(138,24,'This one is painfully specific and very true.',NULL,'2026-06-13 11:00:00',0,0,0,0,0,19,4);
INSERT INTO Post VALUES(139,25,'The three-minute version is brutal but useful.',NULL,'2026-06-13 11:03:00',0,0,0,0,0,25,5);
INSERT INTO Post VALUES(140,26,'Please share the map before finals week.',NULL,'2026-06-13 11:06:00',0,0,0,0,0,23,5);
INSERT INTO Post VALUES(141,27,'Clean monolith first is the sane student-project strategy.',NULL,'2026-06-13 11:09:00',0,0,0,0,0,22,4);
INSERT INTO Post VALUES(142,28,'Beginner pace makes this much less intimidating.',NULL,'2026-06-13 11:12:00',0,0,0,0,0,2,1);
INSERT INTO Post VALUES(143,29,'A joins cheat sheet would be a perfect seminar handout.',NULL,'2026-06-13 11:15:00',0,0,0,0,0,6,2);
INSERT INTO Post VALUES(144,30,'Simple checklists beat fancy productivity tools for me.',NULL,'2026-06-13 11:18:00',0,0,0,0,0,9,5);
INSERT INTO Post VALUES(145,31,'Positive note plus risk note is a good review habit.',NULL,'2026-06-13 11:21:00',0,0,0,0,0,18,4);
INSERT INTO Post VALUES(146,32,'Adding vegan options is a nice touch.',NULL,'2026-06-13 11:24:00',0,0,0,0,0,21,5);
INSERT INTO Post VALUES(147,13,'System design resources are easier to follow with examples.',NULL,'2026-06-13 11:27:00',0,0,0,0,0,10,4);
INSERT INTO Post VALUES(148,16,'A missing dockerignore is such a classic mistake.',NULL,'2026-06-13 11:30:00',0,0,0,0,0,14,4);
INSERT INTO Post VALUES(149,16,'Reading stack traces calmly is underrated.',NULL,'2026-06-13 11:33:00',0,0,0,0,0,12,4);
INSERT INTO Post VALUES(150,19,'This is useful for visiting friends, thanks.',NULL,'2026-06-13 11:36:00',0,0,0,0,0,18,5);
INSERT INTO Post VALUES(151,74,'Definition of done helped our group avoid vague endings.',NULL,'2026-06-13 11:39:00',0,0,0,0,0,20,7);
INSERT INTO Post VALUES(154,NULL,'hola',NULL,'2026-06-15 14:36:27',0,0,0,0,0,28,1);
INSERT INTO Post VALUES(155,NULL,'Chavalees vamos a codear',NULL,'2026-06-19 17:18:13',1,1,0,1,0,27,4);
INSERT INTO Post VALUES(156,155,'ni de conya',NULL,'2026-06-19 17:18:27',0,0,0,0,0,27,4);
CREATE TABLE Block (
    blocker_id INTEGER,
    blocked_id INTEGER,
    reason TEXT,
    is_admin_ban INTEGER DEFAULT 0,
    PRIMARY KEY (blocker_id, blocked_id),
    FOREIGN KEY (blocker_id) REFERENCES User(user_id) ON DELETE CASCADE,
    FOREIGN KEY (blocked_id) REFERENCES User(user_id) ON DELETE CASCADE
);
INSERT INTO Block VALUES(27,13,'PorBobi',1);
INSERT INTO Block VALUES(29,27,NULL,0);
CREATE TABLE Follows (
    follower_id INTEGER,
    followed_id INTEGER,
    PRIMARY KEY (follower_id, followed_id),
    FOREIGN KEY (follower_id) REFERENCES User(user_id) ON DELETE CASCADE,
    FOREIGN KEY (followed_id) REFERENCES User(user_id) ON DELETE CASCADE
);
INSERT INTO Follows VALUES(5,27);
CREATE TABLE Vote (
    user_id INTEGER,
    post_id INTEGER,
    type_of_vote INTEGER NOT NULL CHECK(type_of_vote IN (1, -1)), --'1' is upvote and '-1' is downvote
    PRIMARY KEY (user_id, post_id),
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE,
    FOREIGN KEY (post_id) REFERENCES Post(post_id) ON DELETE CASCADE
);
INSERT INTO Vote VALUES(1,5,1);
INSERT INTO Vote VALUES(2,5,1);
INSERT INTO Vote VALUES(23,5,1);
INSERT INTO Vote VALUES(24,5,1);
INSERT INTO Vote VALUES(25,5,1);
INSERT INTO Vote VALUES(26,5,1);
INSERT INTO Vote VALUES(22,6,1);
INSERT INTO Vote VALUES(23,6,1);
INSERT INTO Vote VALUES(24,6,1);
INSERT INTO Vote VALUES(25,6,1);
INSERT INTO Vote VALUES(26,6,-1);
INSERT INTO Vote VALUES(21,7,1);
INSERT INTO Vote VALUES(22,7,-1);
INSERT INTO Vote VALUES(23,7,-1);
INSERT INTO Vote VALUES(24,7,-1);
INSERT INTO Vote VALUES(1,8,1);
INSERT INTO Vote VALUES(2,8,1);
INSERT INTO Vote VALUES(3,8,1);
INSERT INTO Vote VALUES(4,8,1);
INSERT INTO Vote VALUES(5,8,1);
INSERT INTO Vote VALUES(6,8,-1);
INSERT INTO Vote VALUES(20,8,1);
INSERT INTO Vote VALUES(21,8,1);
INSERT INTO Vote VALUES(22,8,1);
INSERT INTO Vote VALUES(23,8,1);
INSERT INTO Vote VALUES(24,8,1);
INSERT INTO Vote VALUES(25,8,1);
INSERT INTO Vote VALUES(26,8,1);
INSERT INTO Vote VALUES(19,9,1);
INSERT INTO Vote VALUES(20,9,1);
INSERT INTO Vote VALUES(21,9,1);
INSERT INTO Vote VALUES(22,9,1);
INSERT INTO Vote VALUES(23,9,1);
INSERT INTO Vote VALUES(18,10,1);
INSERT INTO Vote VALUES(19,10,1);
INSERT INTO Vote VALUES(1,11,1);
INSERT INTO Vote VALUES(2,11,1);
INSERT INTO Vote VALUES(3,11,1);
INSERT INTO Vote VALUES(4,11,1);
INSERT INTO Vote VALUES(5,11,1);
INSERT INTO Vote VALUES(6,11,1);
INSERT INTO Vote VALUES(7,11,1);
INSERT INTO Vote VALUES(8,11,1);
INSERT INTO Vote VALUES(9,11,-1);
INSERT INTO Vote VALUES(17,11,1);
INSERT INTO Vote VALUES(18,11,1);
INSERT INTO Vote VALUES(19,11,1);
INSERT INTO Vote VALUES(20,11,1);
INSERT INTO Vote VALUES(21,11,1);
INSERT INTO Vote VALUES(22,11,1);
INSERT INTO Vote VALUES(23,11,1);
INSERT INTO Vote VALUES(24,11,1);
INSERT INTO Vote VALUES(25,11,1);
INSERT INTO Vote VALUES(26,11,1);
INSERT INTO Vote VALUES(1,12,1);
INSERT INTO Vote VALUES(2,12,1);
INSERT INTO Vote VALUES(3,12,1);
INSERT INTO Vote VALUES(16,12,1);
INSERT INTO Vote VALUES(17,12,1);
INSERT INTO Vote VALUES(18,12,1);
INSERT INTO Vote VALUES(19,12,1);
INSERT INTO Vote VALUES(20,12,1);
INSERT INTO Vote VALUES(21,12,1);
INSERT INTO Vote VALUES(22,12,1);
INSERT INTO Vote VALUES(23,12,1);
INSERT INTO Vote VALUES(24,12,1);
INSERT INTO Vote VALUES(25,12,1);
INSERT INTO Vote VALUES(26,12,1);
INSERT INTO Vote VALUES(15,13,1);
INSERT INTO Vote VALUES(16,13,1);
INSERT INTO Vote VALUES(17,13,1);
INSERT INTO Vote VALUES(18,13,1);
INSERT INTO Vote VALUES(19,13,1);
INSERT INTO Vote VALUES(20,13,1);
INSERT INTO Vote VALUES(21,13,1);
INSERT INTO Vote VALUES(22,13,1);
INSERT INTO Vote VALUES(23,13,1);
INSERT INTO Vote VALUES(24,13,-1);
INSERT INTO Vote VALUES(14,14,1);
INSERT INTO Vote VALUES(15,14,1);
INSERT INTO Vote VALUES(16,14,1);
INSERT INTO Vote VALUES(17,14,1);
INSERT INTO Vote VALUES(18,14,1);
INSERT INTO Vote VALUES(19,14,1);
INSERT INTO Vote VALUES(20,14,1);
INSERT INTO Vote VALUES(14,15,1);
INSERT INTO Vote VALUES(15,15,1);
INSERT INTO Vote VALUES(16,15,1);
INSERT INTO Vote VALUES(17,15,-1);
INSERT INTO Vote VALUES(18,15,-1);
INSERT INTO Vote VALUES(12,16,1);
INSERT INTO Vote VALUES(11,17,1);
INSERT INTO Vote VALUES(12,17,1);
INSERT INTO Vote VALUES(14,17,1);
INSERT INTO Vote VALUES(15,17,1);
INSERT INTO Vote VALUES(16,17,1);
INSERT INTO Vote VALUES(17,17,1);
INSERT INTO Vote VALUES(18,17,1);
INSERT INTO Vote VALUES(19,17,1);
INSERT INTO Vote VALUES(20,17,1);
INSERT INTO Vote VALUES(21,17,1);
INSERT INTO Vote VALUES(22,17,1);
INSERT INTO Vote VALUES(23,17,1);
INSERT INTO Vote VALUES(24,17,1);
INSERT INTO Vote VALUES(25,17,1);
INSERT INTO Vote VALUES(26,17,1);
INSERT INTO Vote VALUES(10,18,1);
INSERT INTO Vote VALUES(11,18,1);
INSERT INTO Vote VALUES(12,18,1);
INSERT INTO Vote VALUES(14,18,1);
INSERT INTO Vote VALUES(15,18,1);
INSERT INTO Vote VALUES(16,18,1);
INSERT INTO Vote VALUES(17,18,1);
INSERT INTO Vote VALUES(18,18,1);
INSERT INTO Vote VALUES(19,18,1);
INSERT INTO Vote VALUES(20,18,-1);
INSERT INTO Vote VALUES(9,19,1);
INSERT INTO Vote VALUES(10,19,1);
INSERT INTO Vote VALUES(11,19,1);
INSERT INTO Vote VALUES(8,20,1);
INSERT INTO Vote VALUES(9,20,1);
INSERT INTO Vote VALUES(10,20,1);
INSERT INTO Vote VALUES(11,20,1);
INSERT INTO Vote VALUES(12,20,1);
INSERT INTO Vote VALUES(14,20,-1);
INSERT INTO Vote VALUES(15,20,-1);
INSERT INTO Vote VALUES(7,21,1);
INSERT INTO Vote VALUES(8,21,1);
INSERT INTO Vote VALUES(9,21,1);
INSERT INTO Vote VALUES(10,21,1);
INSERT INTO Vote VALUES(11,21,1);
INSERT INTO Vote VALUES(12,21,1);
INSERT INTO Vote VALUES(14,21,1);
INSERT INTO Vote VALUES(15,21,1);
INSERT INTO Vote VALUES(16,21,1);
INSERT INTO Vote VALUES(17,21,1);
INSERT INTO Vote VALUES(18,21,1);
INSERT INTO Vote VALUES(6,22,1);
INSERT INTO Vote VALUES(7,22,1);
INSERT INTO Vote VALUES(8,22,1);
INSERT INTO Vote VALUES(9,22,1);
INSERT INTO Vote VALUES(10,22,1);
INSERT INTO Vote VALUES(11,22,1);
INSERT INTO Vote VALUES(5,23,1);
INSERT INTO Vote VALUES(6,23,1);
INSERT INTO Vote VALUES(7,23,1);
INSERT INTO Vote VALUES(8,23,1);
INSERT INTO Vote VALUES(9,23,1);
INSERT INTO Vote VALUES(10,23,1);
INSERT INTO Vote VALUES(11,23,1);
INSERT INTO Vote VALUES(12,23,1);
INSERT INTO Vote VALUES(14,23,1);
INSERT INTO Vote VALUES(15,23,1);
INSERT INTO Vote VALUES(16,23,1);
INSERT INTO Vote VALUES(17,23,1);
INSERT INTO Vote VALUES(18,23,1);
INSERT INTO Vote VALUES(19,23,1);
INSERT INTO Vote VALUES(20,23,1);
INSERT INTO Vote VALUES(21,23,1);
INSERT INTO Vote VALUES(22,23,1);
INSERT INTO Vote VALUES(23,23,1);
INSERT INTO Vote VALUES(24,23,1);
INSERT INTO Vote VALUES(25,23,1);
INSERT INTO Vote VALUES(26,23,-1);
INSERT INTO Vote VALUES(4,24,1);
INSERT INTO Vote VALUES(5,24,1);
INSERT INTO Vote VALUES(6,24,1);
INSERT INTO Vote VALUES(7,24,1);
INSERT INTO Vote VALUES(8,24,1);
INSERT INTO Vote VALUES(9,24,1);
INSERT INTO Vote VALUES(10,24,1);
INSERT INTO Vote VALUES(11,24,1);
INSERT INTO Vote VALUES(12,24,1);
INSERT INTO Vote VALUES(14,24,1);
INSERT INTO Vote VALUES(15,24,1);
INSERT INTO Vote VALUES(16,24,1);
INSERT INTO Vote VALUES(17,24,1);
INSERT INTO Vote VALUES(18,24,1);
INSERT INTO Vote VALUES(19,24,1);
INSERT INTO Vote VALUES(20,24,1);
INSERT INTO Vote VALUES(21,24,1);
INSERT INTO Vote VALUES(22,24,1);
INSERT INTO Vote VALUES(23,24,-1);
INSERT INTO Vote VALUES(24,24,-1);
INSERT INTO Vote VALUES(3,25,1);
INSERT INTO Vote VALUES(4,25,1);
INSERT INTO Vote VALUES(5,25,1);
INSERT INTO Vote VALUES(6,25,1);
INSERT INTO Vote VALUES(7,25,1);
INSERT INTO Vote VALUES(8,25,1);
INSERT INTO Vote VALUES(9,25,1);
INSERT INTO Vote VALUES(10,25,1);
INSERT INTO Vote VALUES(11,25,1);
INSERT INTO Vote VALUES(12,25,1);
INSERT INTO Vote VALUES(14,25,1);
INSERT INTO Vote VALUES(15,25,1);
INSERT INTO Vote VALUES(16,25,1);
INSERT INTO Vote VALUES(17,25,1);
INSERT INTO Vote VALUES(2,26,1);
INSERT INTO Vote VALUES(3,26,1);
INSERT INTO Vote VALUES(4,26,1);
INSERT INTO Vote VALUES(5,26,1);
INSERT INTO Vote VALUES(6,26,1);
INSERT INTO Vote VALUES(7,26,1);
INSERT INTO Vote VALUES(8,26,1);
INSERT INTO Vote VALUES(9,26,1);
INSERT INTO Vote VALUES(10,26,-1);
INSERT INTO Vote VALUES(1,27,1);
INSERT INTO Vote VALUES(2,27,1);
INSERT INTO Vote VALUES(3,27,1);
INSERT INTO Vote VALUES(4,27,1);
INSERT INTO Vote VALUES(5,27,1);
INSERT INTO Vote VALUES(6,27,1);
INSERT INTO Vote VALUES(7,27,1);
INSERT INTO Vote VALUES(8,27,1);
INSERT INTO Vote VALUES(9,27,1);
INSERT INTO Vote VALUES(10,27,1);
INSERT INTO Vote VALUES(11,27,1);
INSERT INTO Vote VALUES(12,27,1);
INSERT INTO Vote VALUES(14,27,1);
INSERT INTO Vote VALUES(15,27,1);
INSERT INTO Vote VALUES(16,27,1);
INSERT INTO Vote VALUES(17,27,1);
INSERT INTO Vote VALUES(18,27,-1);
INSERT INTO Vote VALUES(19,27,-1);
INSERT INTO Vote VALUES(20,27,-1);
INSERT INTO Vote VALUES(1,28,1);
INSERT INTO Vote VALUES(2,28,1);
INSERT INTO Vote VALUES(3,28,1);
INSERT INTO Vote VALUES(4,28,1);
INSERT INTO Vote VALUES(5,28,1);
INSERT INTO Vote VALUES(26,28,1);
INSERT INTO Vote VALUES(1,29,1);
INSERT INTO Vote VALUES(2,29,1);
INSERT INTO Vote VALUES(3,29,1);
INSERT INTO Vote VALUES(4,29,1);
INSERT INTO Vote VALUES(5,29,1);
INSERT INTO Vote VALUES(6,29,1);
INSERT INTO Vote VALUES(7,29,1);
INSERT INTO Vote VALUES(8,29,1);
INSERT INTO Vote VALUES(9,29,1);
INSERT INTO Vote VALUES(25,29,1);
INSERT INTO Vote VALUES(26,29,1);
INSERT INTO Vote VALUES(1,30,1);
INSERT INTO Vote VALUES(24,30,1);
INSERT INTO Vote VALUES(25,30,1);
INSERT INTO Vote VALUES(26,30,1);
INSERT INTO Vote VALUES(1,31,1);
INSERT INTO Vote VALUES(2,31,1);
INSERT INTO Vote VALUES(3,31,1);
INSERT INTO Vote VALUES(4,31,1);
INSERT INTO Vote VALUES(5,31,1);
INSERT INTO Vote VALUES(6,31,1);
INSERT INTO Vote VALUES(7,31,1);
INSERT INTO Vote VALUES(8,31,1);
INSERT INTO Vote VALUES(9,31,1);
INSERT INTO Vote VALUES(10,31,-1);
INSERT INTO Vote VALUES(23,31,1);
INSERT INTO Vote VALUES(24,31,1);
INSERT INTO Vote VALUES(25,31,1);
INSERT INTO Vote VALUES(26,31,1);
INSERT INTO Vote VALUES(22,32,1);
INSERT INTO Vote VALUES(23,32,-1);
INSERT INTO Vote VALUES(24,32,-1);
INSERT INTO Vote VALUES(25,32,-1);
INSERT INTO Vote VALUES(11,69,1);
INSERT INTO Vote VALUES(12,69,1);
INSERT INTO Vote VALUES(14,69,1);
INSERT INTO Vote VALUES(15,69,1);
INSERT INTO Vote VALUES(16,69,1);
INSERT INTO Vote VALUES(17,69,1);
INSERT INTO Vote VALUES(18,69,1);
INSERT INTO Vote VALUES(19,69,1);
INSERT INTO Vote VALUES(20,69,1);
INSERT INTO Vote VALUES(21,69,1);
INSERT INTO Vote VALUES(22,69,1);
INSERT INTO Vote VALUES(23,69,-1);
INSERT INTO Vote VALUES(10,70,1);
INSERT INTO Vote VALUES(11,70,1);
INSERT INTO Vote VALUES(12,70,1);
INSERT INTO Vote VALUES(14,70,1);
INSERT INTO Vote VALUES(15,70,1);
INSERT INTO Vote VALUES(16,70,1);
INSERT INTO Vote VALUES(17,70,1);
INSERT INTO Vote VALUES(18,70,1);
INSERT INTO Vote VALUES(9,71,1);
INSERT INTO Vote VALUES(10,71,1);
INSERT INTO Vote VALUES(11,71,1);
INSERT INTO Vote VALUES(12,71,1);
INSERT INTO Vote VALUES(14,71,1);
INSERT INTO Vote VALUES(15,71,1);
INSERT INTO Vote VALUES(16,71,1);
INSERT INTO Vote VALUES(17,71,1);
INSERT INTO Vote VALUES(18,71,1);
INSERT INTO Vote VALUES(19,71,1);
INSERT INTO Vote VALUES(20,71,-1);
INSERT INTO Vote VALUES(8,72,1);
INSERT INTO Vote VALUES(9,72,1);
INSERT INTO Vote VALUES(10,72,1);
INSERT INTO Vote VALUES(11,72,1);
INSERT INTO Vote VALUES(12,72,1);
INSERT INTO Vote VALUES(7,73,1);
INSERT INTO Vote VALUES(8,73,1);
INSERT INTO Vote VALUES(9,73,1);
INSERT INTO Vote VALUES(10,73,1);
INSERT INTO Vote VALUES(11,73,1);
INSERT INTO Vote VALUES(12,73,1);
INSERT INTO Vote VALUES(6,74,1);
INSERT INTO Vote VALUES(7,74,1);
INSERT INTO Vote VALUES(8,74,1);
INSERT INTO Vote VALUES(9,74,-1);
INSERT INTO Vote VALUES(10,74,-1);
INSERT INTO Vote VALUES(5,75,1);
INSERT INTO Vote VALUES(6,75,1);
INSERT INTO Vote VALUES(7,75,1);
INSERT INTO Vote VALUES(8,75,1);
INSERT INTO Vote VALUES(9,75,1);
INSERT INTO Vote VALUES(10,75,1);
INSERT INTO Vote VALUES(11,75,1);
INSERT INTO Vote VALUES(12,75,1);
INSERT INTO Vote VALUES(14,75,1);
INSERT INTO Vote VALUES(4,76,1);
INSERT INTO Vote VALUES(5,76,1);
INSERT INTO Vote VALUES(6,76,-1);
INSERT INTO Vote VALUES(3,77,1);
INSERT INTO Vote VALUES(4,77,1);
INSERT INTO Vote VALUES(5,77,1);
INSERT INTO Vote VALUES(6,77,1);
INSERT INTO Vote VALUES(2,78,1);
INSERT INTO Vote VALUES(1,13,1);
INSERT INTO Vote VALUES(1,6,1);
INSERT INTO Vote VALUES(27,155,1);
CREATE TABLE UserInGroup (
    user_id INTEGER,
    group_id INTEGER,
    PRIMARY KEY (user_id, group_id),
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE,
    FOREIGN KEY (group_id) REFERENCES "Group"(group_id) ON DELETE CASCADE
);
INSERT INTO UserInGroup VALUES(1,1);
INSERT INTO UserInGroup VALUES(1,2);
INSERT INTO UserInGroup VALUES(14,1);
INSERT INTO UserInGroup VALUES(15,1);
INSERT INTO UserInGroup VALUES(16,1);
INSERT INTO UserInGroup VALUES(17,1);
INSERT INTO UserInGroup VALUES(18,1);
INSERT INTO UserInGroup VALUES(19,1);
INSERT INTO UserInGroup VALUES(20,1);
INSERT INTO UserInGroup VALUES(21,1);
INSERT INTO UserInGroup VALUES(22,1);
INSERT INTO UserInGroup VALUES(23,1);
INSERT INTO UserInGroup VALUES(24,1);
INSERT INTO UserInGroup VALUES(25,1);
INSERT INTO UserInGroup VALUES(26,1);
INSERT INTO UserInGroup VALUES(4,2);
INSERT INTO UserInGroup VALUES(5,2);
INSERT INTO UserInGroup VALUES(6,2);
INSERT INTO UserInGroup VALUES(7,2);
INSERT INTO UserInGroup VALUES(8,2);
INSERT INTO UserInGroup VALUES(9,2);
INSERT INTO UserInGroup VALUES(10,2);
INSERT INTO UserInGroup VALUES(2,4);
INSERT INTO UserInGroup VALUES(3,4);
INSERT INTO UserInGroup VALUES(4,4);
INSERT INTO UserInGroup VALUES(5,4);
INSERT INTO UserInGroup VALUES(6,4);
INSERT INTO UserInGroup VALUES(7,4);
INSERT INTO UserInGroup VALUES(8,4);
INSERT INTO UserInGroup VALUES(9,4);
INSERT INTO UserInGroup VALUES(10,4);
INSERT INTO UserInGroup VALUES(11,4);
INSERT INTO UserInGroup VALUES(12,4);
INSERT INTO UserInGroup VALUES(14,4);
INSERT INTO UserInGroup VALUES(15,4);
INSERT INTO UserInGroup VALUES(16,4);
INSERT INTO UserInGroup VALUES(17,4);
INSERT INTO UserInGroup VALUES(2,5);
INSERT INTO UserInGroup VALUES(3,5);
INSERT INTO UserInGroup VALUES(4,5);
INSERT INTO UserInGroup VALUES(5,5);
INSERT INTO UserInGroup VALUES(14,5);
INSERT INTO UserInGroup VALUES(15,5);
INSERT INTO UserInGroup VALUES(16,5);
INSERT INTO UserInGroup VALUES(17,5);
INSERT INTO UserInGroup VALUES(18,5);
INSERT INTO UserInGroup VALUES(19,5);
INSERT INTO UserInGroup VALUES(20,5);
INSERT INTO UserInGroup VALUES(21,5);
INSERT INTO UserInGroup VALUES(19,7);
INSERT INTO UserInGroup VALUES(21,7);
INSERT INTO UserInGroup VALUES(16,7);
INSERT INTO UserInGroup VALUES(23,7);
INSERT INTO UserInGroup VALUES(15,7);
INSERT INTO UserInGroup VALUES(22,7);
INSERT INTO UserInGroup VALUES(17,7);
INSERT INTO UserInGroup VALUES(24,7);
INSERT INTO UserInGroup VALUES(20,7);
INSERT INTO UserInGroup VALUES(18,7);
INSERT INTO UserInGroup VALUES(27,5);
INSERT INTO UserInGroup VALUES(27,8);
INSERT INTO UserInGroup VALUES(27,1);
INSERT INTO UserInGroup VALUES(1,4);
INSERT INTO UserInGroup VALUES(1,5);
INSERT INTO UserInGroup VALUES(1,7);
INSERT INTO UserInGroup VALUES(28,1);
INSERT INTO UserInGroup VALUES(27,4);
CREATE TABLE GroupJoinRequest (
    user_id INTEGER NOT NULL,
    group_id INTEGER NOT NULL,
    requested_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, group_id),
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE,
    FOREIGN KEY (group_id) REFERENCES "Group"(group_id) ON DELETE CASCADE
);
PRAGMA writable_schema=ON;
CREATE TABLE IF NOT EXISTS sqlite_sequence(name,seq);
DELETE FROM sqlite_sequence;
INSERT INTO sqlite_sequence VALUES('User',29);
INSERT INTO sqlite_sequence VALUES('Group',8);
INSERT INTO sqlite_sequence VALUES('Post',156);
PRAGMA writable_schema=OFF;
COMMIT;
