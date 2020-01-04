import { PretragaElasticService } from './pretraga-elastic.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-pretraga-elastic',
  templateUrl: './pretraga-elastic.component.html',
  styleUrls: ['./pretraga-elastic.component.css']
})
export class PretragaElasticComponent implements OnInit {

  kriterijumPretrage: String;
  constructor(private pretragaService: PretragaElasticService) { }

  ngOnInit() {
  }

  PretragaPoPoljima(event){
    event.preventDefault();
    const target = event.target;
    this.pretragaService.pretraziTermove(target, this.kriterijumPretrage);
  }

}
