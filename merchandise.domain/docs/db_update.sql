-- Function: checkvector(tsvector, tsquery)

-- DROP FUNCTION checkvector(tsvector, tsquery);

CREATE OR REPLACE FUNCTION checkvector(x tsvector, y tsquery)
  RETURNS boolean AS
$BODY$BEGIN
RETURN x@@y;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION checkvector(tsvector, tsquery)
  OWNER TO postgres;
