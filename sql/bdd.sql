create or replace package Tracker IS
    procedure set_flag_1(le_isbn VARCHAR2);
    procedure set_flag_2(le_isbn VARCHAR2);
end;

create or replace package body TRACKER IS
    procedure set_flag_1(le_isbn VARCHAR2) is
    begin
        set transaction read write;
        update TRACKING
        set FLAG = 1
        where isbn = le_isbn;
        commit;
    exception
        when others then
            if SQLCODE = -01438 then
                rollback;
                raise_application_error(-20001, 'un argument trop long');
            else
                rollback;
                raise_application_error(-20099, 'HAPPENS' || SQLCODE);
            end if;
    end;

    procedure set_flag_2(le_isbn VARCHAR2) is
    begin
        set transaction read write;
        update TRACKING
        set FLAG = 2
        where isbn = le_isbn;
        commit;
    exception
        when others then
            if SQLCODE = -01438 then
                rollback;
                raise_application_error(-20001, 'un argument trop long');
            else
                rollback;
                raise_application_error(-20099, 'HAPPENS' || SQLCODE);
            end if;
    end;
end;

create or replace TRIGGER tracking AFTER INSERT ON EXEMPLAIREEMPRUNT FOR EACH ROW
DECLARE 
adherent_nom adherent.nom%TYPE;
adherent_prenom adherent.prenom%TYPE;
exemplaire_isbn exemplaire.isbn%TYPE;
BEGIN
    SELECT nom, prenom INTO adherent_nom, adherent_prenom FROM adherent WHERE numadh = :new.numeroAdherent;
    SELECT isbnn INTO exemplaire_isbn FROM exemplaire WHERE numeroInventaire = :new.numeroInventaire;
    INSERT INTO Tracking(nom, prenom, isbn) values(adherent_nom, adherent_prenom, exemplaire_isbn);
END;


select * from exemplaire;