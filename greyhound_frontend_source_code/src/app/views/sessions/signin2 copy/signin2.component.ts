import { Component, OnInit } from '@angular/core';
import { UntypedFormGroup, UntypedFormBuilder, UntypedFormControl, Validators, FormGroup } from '@angular/forms';
import { SessionsService } from '../sessions.service';

@Component({
  selector: 'app-signin2',
  templateUrl: './signin2.component.html',
  styleUrls: ['./signin2.component.scss']
})
export class Signin2Component implements OnInit {

  signupForm: FormGroup;

  constructor(
    private _sessionService: SessionsService) {
      
    this.signupForm = _sessionService.createForm();
    }

  ngOnInit() {
  }

  onSubmit() {
    const data = this.signupForm.getRawValue();
    this._sessionService.login(data);
}
}
