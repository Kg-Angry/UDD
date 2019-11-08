import { AppRoutingModule } from './app-routing.module';
import { RouterModule } from '@angular/router';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { RegistrationComponent } from './registration/registration.component';
import { NaucniCasopisComponent } from './naucni-casopis/naucni-casopis.component';
import { NaucnaOblastComponent } from './naucna-oblast/naucna-oblast.component';
import { NaucniRadoviComponent } from './naucni-radovi/naucni-radovi.component';
import { UserProfileComponent } from './user-profile/user-profile.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    RegistrationComponent,
    NaucniCasopisComponent,
    NaucnaOblastComponent,
    NaucniRadoviComponent,
    UserProfileComponent
  ],
  imports: [
    BrowserModule,
    RouterModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
