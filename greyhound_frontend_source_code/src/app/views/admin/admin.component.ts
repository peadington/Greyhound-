import { Component, OnInit } from '@angular/core';
import { UtilityService } from '../../shared/services/utility.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent implements OnInit {

  fixedInViewport: boolean = false;
  isOpened: boolean = true;
  mode: any = "side";
  sessionUser: any;

  constructor(
    public _utilityService: UtilityService
  ) {
    this.sessionUser = _utilityService.getSessionUser();
  }

  ngOnInit() {
    if (window.outerWidth <= 600) {
      this.isOpened = false;
      this.fixedInViewport = true;
      this.mode = "over";
    }
  }
}
