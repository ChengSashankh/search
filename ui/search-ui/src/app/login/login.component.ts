import { Component } from '@angular/core';
import {AuthService} from "@auth0/auth0-angular";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {


  constructor(public auth: AuthService, private router: Router) { }

  openSecretDoor() {
    this.auth.isAuthenticated$
      .subscribe(isAuthenticated => {
        if (!isAuthenticated) {
          this.auth.loginWithRedirect().subscribe(() => {
            this.router.navigate(['search']);
          })
        } else {
          this.router.navigate(['search']);
        }
      })
  }
}
