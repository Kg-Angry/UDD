import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class PretragaElasticService {

  constructor(private http: HttpClient) { }

  pretraziTermove(target, kriterijumPretrage) {

    let term = target.querySelector('input[name=\'term\']').value;
    console.log('term ' + term);
    console.log('Kriterijum ' + kriterijumPretrage);
    return this.http.post('api/pretraga/term', {kriterijum: kriterijumPretrage, upit: term})
    .subscribe(data => {console.log('Uspesna pretraga ' + data);});
  }
}
