import { NaucnaOblast } from './naucna-oblast';
import { Korisnik } from './korisnik';
export class NaucniCasopis {
  id: number;
  naziv: String;
  issn: number;
  tipCasopisa: String;
  glavni_urednik: Korisnik;
  urednici: Korisnik[];
  recenzenti: Korisnik[];
  naucna_oblast: NaucnaOblast[];
  status: boolean;
}
