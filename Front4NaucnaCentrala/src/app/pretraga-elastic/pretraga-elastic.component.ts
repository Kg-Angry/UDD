import { NaucniCasopis } from './../class/naucni-casopis';
import { ResultRetriever } from './../class/result-retriever';
import { PretragaElasticService } from './pretraga-elastic.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-pretraga-elastic',
  templateUrl: './pretraga-elastic.component.html',
  styleUrls: ['./pretraga-elastic.component.css']
})
export class PretragaElasticComponent implements OnInit {

  kriterijumPretrage: String;
  rezultat: ResultRetriever[] = JSON.parse(localStorage.getItem('document'));
  casopisi: NaucniCasopis[] = JSON.parse(localStorage.getItem('casopisi'));

  constructor(private pretragaService: PretragaElasticService) { }

  ngOnInit() {
  }

  PretragaPoPoljima(event){
    event.preventDefault();
    const target = event.target;
    this.pretragaService.pretraziTermove(target, this.kriterijumPretrage);
  }

  Preuzmi(naslovRada: String){
    console.log('Preuzimanje');
    this.pretragaService.preuzmiRad(naslovRada);
  }
  Kupi(naslovRada: String){
    this.pretragaService.kupiRad();
  }

}
