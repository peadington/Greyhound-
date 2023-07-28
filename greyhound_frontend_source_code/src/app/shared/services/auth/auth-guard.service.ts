import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';
import { SessionsService } from 'app/views/sessions/sessions.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate {

  constructor(public _sessionService: SessionsService, public router: Router) { }

  canActivate(): boolean {
    if (!this._sessionService.isAuthenticated()) {
      this.router.navigateByUrl('/sessions/signin');
      return false;
    }
    return true;
  }
}