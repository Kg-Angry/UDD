import { Korisnik } from './../class/korisnik';
import { NaucniCasopis } from './../class/naucni-casopis';
import { ResultRetriever } from './../class/result-retriever';
import { PretragaElasticService } from './pretraga-elastic.service';
import { Component, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-pretraga-elastic',
  templateUrl: './pretraga-elastic.component.html',
  styleUrls: ['./pretraga-elastic.component.css']
})
export class PretragaElasticComponent implements OnInit {

  kriterijumPretrage: String = '';
  rezultat: ResultRetriever[] = JSON.parse(localStorage.getItem('document'));
  casopisi: NaucniCasopis[] = JSON.parse(localStorage.getItem('casopisi'));
  AndOr: String = '';
  MoreLike = false;
  korisnici: Korisnik[] = JSON.parse(localStorage.getItem('korisnici'));

  constructor(private pretragaService: PretragaElasticService) { }

  ngOnInit() {
  }

  PretragaPoPoljima(event){
    event.preventDefault();
    const target = event.target;
    if(this.MoreLike === false){
      this.pretragaService.pretraziTermove(target, this.kriterijumPretrage);
    } else{
      this.pretragaService.moreLikeThisPretraga(target);
    }

  }

  Preuzmi(naslovRada: String){
    this.pretragaService.preuzmiRad(naslovRada);
  }
  Kupi(naslovRada: String){
    this.pretragaService.kupiRad();
  }
  Proba($event){
    event.preventDefault();
    const target = event.target;
    this.pretragaService.naprednaPretraga(target, this.AndOr);
  }

  GeoProstornaPretraga(nazivCasiopisa: String){


    this.pretragaService.GeoProstornaPretraga(nazivCasiopisa);
  }

}
