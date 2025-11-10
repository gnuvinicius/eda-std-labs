listar sequences no postgresql

SELECT relname AS sequence_name
FROM pg_class
WHERE relkind = 'S'
ORDER BY relname;



DROP SEQUENCE IF EXISTS nome_da_sequence;


DROP SEQUENCE IF EXISTS (
    SELECT relname AS sequence_name
    FROM pg_class
    WHERE relkind = 'S'
    ORDER BY relname
);