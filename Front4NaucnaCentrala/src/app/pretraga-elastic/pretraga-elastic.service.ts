import Swal from 'sweetalert2';
import { ResultRetriever } from './../class/result-retriever';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { timer } from 'rxjs';

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
    .subscribe((data: ResultRetriever) => {localStorage.setItem('document', JSON.stringify(data)); location.href='/search'});
  }

  preuzmiRad(naslovRada: String){
    return this.http.post('api/naucni_rad/preuzmi', {naslov: naslovRada}, {responseType: 'blob'})
    .subscribe(data =>
      {
        var newBlob = new Blob([data], { type: "application/pdf" });
        const downloadURL = URL.createObjectURL(newBlob);
        window.open(downloadURL);
      });
  }

  kupiRad(){
    Swal.fire({
      position: 'top-end',
      icon: 'success',
      title: 'Rad je dodat u korpu',
      showConfirmButton: false,
      timer: 2500
    });
      timer(2500).subscribe(t => location.href = '/search');

  }
}
