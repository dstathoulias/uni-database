--SET max_parallel_workers_per_gather = 0;


-- B-Tree type indexes
----------------------------------------------------------------------------
/*CREATE INDEX index_education_country ON education (country);
CREATE INDEX index_advertisement_datePosted ON advertisement ("datePosted");
CREATE INDEX index_jobOffer_fromAge ON "jobOffer" ("fromAge");
CREATE INDEX index_msg_dateSent ON msg ("dateSent");
CREATE INDEX index_education_email ON education (email);
CREATE INDEX index_advertisement_email ON advertisement (email);
CREATE INDEX index_member_email ON member (email);
CREATE INDEX index_msg_senderEmail ON msg ("senderEmail");
CREATE INDEX index_advertisement_advertisementID ON advertisement ("advertisementID");
CREATE INDEX index_jobOffer_advertisementID ON "jobOffer" ("advertisementID");*/
----------------------------------------------------------------------------


-- Hash type indexes
----------------------------------------------------------------------------
/*CREATE INDEX index_education_country ON education USING HASH (country);
CREATE INDEX index_advertisement_datePosted ON advertisement USING HASH ("datePosted");
CREATE INDEX index_jobOffer_fromAge ON "jobOffer" USING HASH ("fromAge");
CREATE INDEX index_msg_dateSent ON msg USING HASH ("dateSent");
CREATE INDEX index_education_email ON education USING HASH (email);
CREATE INDEX index_advertisement_email ON advertisement USING HASH (email);
CREATE INDEX index_member_email ON member USING HASH (email);
CREATE INDEX index_msg_senderEmail ON msg USING HASH ("senderEmail");
CREATE INDEX index_advertisement_advertisementID ON advertisement USING HASH ("advertisementID");
CREATE INDEX index_jobOffer_advertisementID ON "jobOffer" USING HASH ("advertisementID");*/
----------------------------------------------------------------------------


-- Optimal indexes
----------------------------------------------------------------------------
/*CREATE INDEX index_education_country ON education USING HASH (country);
CREATE INDEX index_advertisement_datePosted ON advertisement ("datePosted");
CREATE INDEX index_jobOffer_fromAge ON "jobOffer" ("fromAge");
CREATE INDEX index_msg_dateSent ON msg ("dateSent");
CREATE INDEX index_education_email ON education USING HASH (email);
CREATE INDEX index_advertisement_email ON advertisement USING HASH (email);
CREATE INDEX index_member_email ON member USING HASH (email);
CREATE INDEX index_msg_senderEmail ON msg USING HASH ("senderEmail");
CREATE INDEX index_advertisement_advertisementID ON advertisement USING HASH ("advertisementID");
CREATE INDEX index_jobOffer_advertisementID ON "jobOffer" USING HASH ("advertisementID");*/
----------------------------------------------------------------------------


-- Drop indexes
----------------------------------------------------------------------------
/*DROP INDEX index_education_country;
DROP INDEX index_advertisement_datePosted;
DROP INDEX index_jobOffer_fromAge;
DROP INDEX index_msg_dateSent;
DROP INDEX index_education_email;
DROP INDEX index_advertisement_email;
DROP INDEX index_member_email;
DROP INDEX index_msg_senderEmail;
DROP INDEX index_advertisement_advertisementID;
DROP INDEX index_jobOffer_advertisementID;*/
----------------------------------------------------------------------------


-- Cluster indexes
----------------------------------------------------------------------------
--CLUSTER advertisement USING index_advertisement_datePosted;
--CLUSTER msg USING index_msg_dateSent;
----------------------------------------------------------------------------


-- Enable/Disable algorithms
----------------------------------------------------------------------------
/*SET enable_nestloop = off;
SET enable_hashjoin = off;
SET enable_mergejoin = off;

SET enable_nestloop = on;
SET enable_hashjoin = on;
SET enable_mergejoin = on;*/
----------------------------------------------------------------------------


-- Default query
----------------------------------------------------------------------------
WITH StudiedInCanada AS (
    SELECT email
    FROM education
    WHERE country = 'Canada'
),
PostedRecentAds AS (
    SELECT a.email
    FROM advertisement a
    JOIN "jobOffer" jo ON jo."advertisementID" = a."advertisementID"
    JOIN member m ON a.email = m.email
    WHERE a."datePosted" >= CURRENT_DATE - INTERVAL '6 months'
      AND jo."fromAge" > 21
      AND jo."fromAge" < 30
    GROUP BY a.email
    HAVING COUNT(a."advertisementID") >= 2
),
ReceivedRecentMessages AS (
    SELECT "senderEmail" AS email
    FROM msg
    WHERE "dateSent" >= CURRENT_DATE - INTERVAL '6 months'
)
SELECT dist."eduLevel", COUNT(*) AS "memberCount"
FROM (
    SELECT DISTINCT e.email, e."eduLevel"
    FROM StudiedInCanada sc
    JOIN PostedRecentAds ra ON sc.email = ra.email
	JOIN ReceivedRecentMessages rm ON sc.email = rm.email
    JOIN education e ON sc.email = e.email
    ORDER BY e.email
) AS dist
GROUP BY dist."eduLevel"
ORDER BY "memberCount" DESC;
----------------------------------------------------------------------------


-- Query with new join order
----------------------------------------------------------------------------
/*WITH StudiedInCanada AS (
    SELECT email
    FROM education
    WHERE country = 'Canada'
),
PostedRecentAds AS (
    SELECT a.email
    FROM advertisement a
    JOIN "jobOffer" jo ON jo."advertisementID" = a."advertisementID"
    JOIN member m ON a.email = m.email
    WHERE a."datePosted" >= CURRENT_DATE - INTERVAL '6 months'
      AND jo."fromAge" > 21
      AND jo."fromAge" < 30
    GROUP BY a.email
    HAVING COUNT(a."advertisementID") >= 2
),
ReceivedRecentMessages AS (
    SELECT "senderEmail" AS email
    FROM msg
    WHERE "dateSent" >= CURRENT_DATE - INTERVAL '6 months'
)
SELECT dist."eduLevel", COUNT(*) AS "memberCount"
FROM (
    SELECT DISTINCT e.email, e."eduLevel"
    FROM StudiedInCanada sc
	JOIN ReceivedRecentMessages rm ON sc.email = rm.email
    JOIN education e ON sc.email = e.email
    JOIN PostedRecentAds ra ON sc.email = ra.email
    ORDER BY e.email
) AS dist
GROUP BY dist."eduLevel"
ORDER BY "memberCount" DESC;*/
----------------------------------------------------------------------------


-- Query with new country (Afghanistan)
----------------------------------------------------------------------------
WITH StudiedInCanada AS (
    SELECT email
    FROM education
    WHERE country = 'Afghanistan'
),
PostedRecentAds AS (
    SELECT a.email
    FROM advertisement a
    JOIN "jobOffer" jo ON jo."advertisementID" = a."advertisementID"
    JOIN member m ON a.email = m.email
    WHERE a."datePosted" >= CURRENT_DATE - INTERVAL '6 months'
      AND jo."fromAge" > 21
      AND jo."fromAge" < 30
    GROUP BY a.email
    HAVING COUNT(a."advertisementID") >= 2
),
ReceivedRecentMessages AS (
    SELECT "senderEmail" AS email
    FROM msg
    WHERE "dateSent" >= CURRENT_DATE - INTERVAL '6 months'
)
SELECT dist."eduLevel", COUNT(*) AS "memberCount"
FROM (
    SELECT DISTINCT e.email, e."eduLevel"
    FROM StudiedInCanada sc
    JOIN PostedRecentAds ra ON sc.email = ra.email
	JOIN ReceivedRecentMessages rm ON sc.email = rm.email
    JOIN education e ON sc.email = e.email
    ORDER BY e.email
) AS dist
GROUP BY dist."eduLevel"
ORDER BY "memberCount" DESC;
----------------------------------------------------------------------------


-- Find new country (Afghanistan)
----------------------------------------------------------------------------
--SELECT country, COUNT(*)
--FROM education
--GROUP BY country
--ORDER BY COUNT(*) ASC
--LIMIT 1;