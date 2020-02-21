-- Korisnik
insert into korisnik(aktiviran_nalog,drzava,email,grad,ime,korisnicko_ime,lozinka, prezime,tip_korisnika,titula)
values (1,"Srbija","alex@gmail.com","Kragujevac","Aleksa","kgangry","kgangry","Vasic","UREDNIK","");

insert into korisnik(aktiviran_nalog,drzava,email,grad,ime,korisnicko_ime,lozinka, prezime,tip_korisnika,titula)
values (1,"Srbija","alex@gmail.com","Kragujevac","Mirko","autor","autor","Markovic","AUTOR","");

insert into korisnik(aktiviran_nalog,drzava,email,grad,ime,korisnicko_ime,lozinka, prezime,tip_korisnika,titula)
values (1,"Srbija","alex@gmail.com","Kragujevac","A","admin","admin","V","ADMINISTRATOR","");

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

-- SELECT * from korisnik;