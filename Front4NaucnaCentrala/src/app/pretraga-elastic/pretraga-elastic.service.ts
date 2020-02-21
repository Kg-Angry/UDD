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
    console.log(term);
    if(kriterijumPretrage === '' && term === ''){
      return this.http.post('api/pretraga/all', {kriterijum: kriterijumPretrage, upit: term})
      .subscribe((data: ResultRetriever) => {localStorage.setItem('document', JSON.stringify(data)); location.href='/search'});
    } else if(kriterijumPretrage !== ''){
      return this.http.post('api/pretraga/term', {kriterijum: kriterijumPretrage, upit: term})
      .subscribe((data: ResultRetriever) => {localStorage.setItem('document', JSON.stringify(data)); location.href='/search'});
    } else{
      return this.http.post('api/pretraga/poSvimPoljima',{kriterijum: kriterijumPretrage, upit: term})
      .subscribe((data: ResultRetriever) => {localStorage.setItem('document', JSON.stringify(data)); location.href='/search'});
    }

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

  naprednaPretraga(target,operator){
    const sadrzaj = target.querySelector('input[name=\'sadrzaj\']').value;
    const naslovRada = target.querySelector('input[name=\'naslovRada\']').value;
    const kljucniPojmovi = target.querySelector('input[name=\'kljucniPojmovi\']').value;
    const nazivCasopisa = target.querySelector('input[name=\'nazivCasopisa\']').value;
    const naucnaOblast = target.querySelector('input[name=\'naucnaOblast\']').value;
    const autor = target.querySelector('input[name=\'autor\']').value;

    console.log(sadrzaj);
    console.log(naslovRada);
    console.log(kljucniPojmovi);
    console.log(nazivCasopisa);
    console.log(naucnaOblast);
    console.log(operator);
    console.log(autor);

    if (operator === '' || (sadrzaj === '' && naslovRada === '' && kljucniPojmovi === '' && nazivCasopisa === '' && naucnaOblast === '' && autor === '')){
      Swal.fire({
        position: 'top-end',
        icon: 'error',
        title: 'Mora se izabrati operacija (AND/OR)',
        showConfirmButton: false,
        timer: 2500
      });
      let kriterijumPretrage='';
      let term = '';
      return this.http.post('api/pretraga/all', {kriterijum: kriterijumPretrage, upit: term})
      .subscribe((data: ResultRetriever) => {localStorage.setItem('document', JSON.stringify(data)); location.href='/search'});
    } else{
      return this.http.post('api/pretraga/booleanQuery',{kriterijum1: 'sadrzaj', vrednost1: sadrzaj, kriterijum2: 'naslovRada',
       vrednost2: naslovRada, kriterijum3: 'kljucniPojmovi', vrednost3: kljucniPojmovi,
       kriterijum4: 'nazivCasopisa', vrednost4: nazivCasopisa, kriterijum5: 'nazivNaucneOblasti', vrednost5: naucnaOblast,
       kriterijum6: 'autor', vrednost6: autor, operator: operator}).subscribe((data: ResultRetriever) => {localStorage.setItem('document', JSON.stringify(data)); location.href='/search'})
    }
  }

  moreLikeThisPretraga(target){

    const vrednost = target.querySelector('input[name=\'term\']').value;

    return this.http.post('api/pretraga/moreLike', {upit: vrednost}).subscribe((data: ResultRetriever) =>
    {localStorage.setItem('document', JSON.stringify(data)); location.href = '/search'});
  }
}
