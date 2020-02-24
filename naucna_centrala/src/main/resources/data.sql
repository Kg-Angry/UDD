-- Korisnik
insert into korisnik(aktiviran_nalog,drzava,email,grad,ime,korisnicko_ime,lozinka, prezime,tip_korisnika,titula,longitude,lattitude)
values (1,"Srbija","alex@gmail.com","Kragujevac","Aleksa","kgangry","kgangry","Vasic","UREDNIK","",31.04,45.01);

insert into korisnik(aktiviran_nalog,drzava,email,grad,ime,korisnicko_ime,lozinka, prezime,tip_korisnika,titula,longitude,lattitude)
values (1,"Srbija","alex@gmail.com","Kragujevac","Mirko","autor","autor","Markovic","AUTOR","",32.05,33.46);

insert into korisnik(aktiviran_nalog,drzava,email,grad,ime,korisnicko_ime,lozinka, prezime,tip_korisnika,titula,longitude,lattitude)
values (1,"Srbija","alex@gmail.com","Kragujevac","A","admin","admin","V","ADMINISTRATOR","",32.45,32.88);

-- korisnicko_ime: recenzent, lozinka: recenzent
insert into korisnik (aktiviran_nalog,drzava,email,grad,ime,korisnicko_ime,lozinka,prezime,tip_korisnika,titula,longitude,lattitude)
values (1,'SRB','recenzent@gmail.com','NS','Mirko','recenzent','recenzent','Mirkovic','RECENZENT','MAs',31.00,33.88);
-- korisnicko_ime: recenzent1, lozinka: recenzent1
insert into korisnik (aktiviran_nalog,drzava,email,grad,ime,korisnicko_ime,lozinka,prezime,tip_korisnika,titula,longitude,lattitude)
values (1,'SRB','recenzent1@gmail.com','BG','Dusan','recenzent1','recenzent1','Strugar','RECENZENT','Dipl',32.45,33.88);
-- korisnicko_ime: recenzent2, lozinka: recenzent2
insert into korisnik (aktiviran_nalog,drzava,email,grad,ime,korisnicko_ime,lozinka,prezime,tip_korisnika,titula,longitude,lattitude)
values (1,'SRB','recenzent2@gmail.com','KG','Dragan','recenzent2','recenzent2','Petrovic','RECENZENT','Mas',21.74,21.88);
-- korisnicko_ime: recenzent3, lozinka: recenzent3
insert into korisnik (aktiviran_nalog,drzava,email,grad,ime,korisnicko_ime,lozinka,prezime,tip_korisnika,titula,longitude,lattitude)
values (1,'SRB','recenzent3@gmail.com','SU','Anastasija','recenzent3','recenzent3','Milutinovic','RECENZENT','Mas',32.45,32.88);
-- korisnicko_ime: recenzent4, lozinka: recenzent4
insert into korisnik (aktiviran_nalog,drzava,email,grad,ime,korisnicko_ime,lozinka,prezime,tip_korisnika,titula,longitude,lattitude)
values (1,'SRB','recenzent4@gmail.com','NI','Nikola','recenzent4','recenzent4','Janicijevic','RECENZENT','Mas',-32.45,32.88);


--Naucna oblast
insert into naucna_oblast(naziv,opis) values ("IT","Oblast namenjena za infromacione tehnologije");
insert into naucna_oblast(naziv,opis) values ("Elektrotehnika","Oblast namenjena za rad sa uredjajima koji su tehnicke prirode");
insert into naucna_oblast(naziv,opis) values ("Gradjevinarstvo","Oblast namenjena za izgradnju infrastrukture");

-- Naucni casopis
insert into naucni_casopis(cena,issn,naziv,status,tip_casopisa,glavni_urednik_id)
values(1.1,111,"Top Speed",true,"OPEN_ACCESS",1);
insert into naucni_casopis(cena,issn,naziv,status,tip_casopisa,glavni_urednik_id)
values(2.1,222,"Casopis",true,"SA_PRETPLATOM",1);
insert into naucni_casopis(cena,issn,naziv,status,tip_casopisa,glavni_urednik_id)
values(3.1,333,"Casopis1",true,"OPEN_ACCESS",1);

-- naucne oblasti casopis
insert into casopis_naucneoblasti(casopis_id,naucna_oblast_id)
values(1,1);
insert into casopis_naucneoblasti(casopis_id,naucna_oblast_id)
values(1,2);
insert into casopis_naucneoblasti(casopis_id,naucna_oblast_id)
values(2,2);
insert into casopis_naucneoblasti(casopis_id,naucna_oblast_id)
values(3,1);

-- casopis recenzenti
insert into casopis_recenzenti(casopis_id,recenzent_id)
values(1,4);
insert into casopis_recenzenti(casopis_id,recenzent_id)
values(1,5);
insert into casopis_recenzenti(casopis_id,recenzent_id)
values(1,6);
insert into casopis_recenzenti(casopis_id,recenzent_id)
values(2,7);
insert into casopis_recenzenti(casopis_id,recenzent_id)
values(2,8);
insert into casopis_recenzenti(casopis_id,recenzent_id)
values(3,8);
insert into casopis_recenzenti(casopis_id,recenzent_id)
values(3,7);
insert into casopis_recenzenti(casopis_id,recenzent_id)
values(3,4);

-- SELECT * from korisnik;