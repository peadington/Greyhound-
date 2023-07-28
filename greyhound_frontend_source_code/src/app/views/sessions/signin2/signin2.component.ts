import { Component, OnInit } from '@angular/core';
import { UntypedFormGroup, UntypedFormBuilder, UntypedFormControl, Validators, FormGroup } from '@angular/forms';
import { SessionsService } from '../sessions.service';

@Component({
  selector: 'app-signin2',
  templateUrl: './signin2.component.html',
  styleUrls: ['./signin2.component.scss']
})
export class Signin2Component implements OnInit {

  signinForm: FormGroup;

  constructor(
    private _sessionService: SessionsService) {

    this.signinForm = _sessionService.createForm();
  }

  ngOnInit() {
  }

  signin() {
    const data = this.signinForm.getRawValue();
    this._sessionService.login(data);
  }
}
