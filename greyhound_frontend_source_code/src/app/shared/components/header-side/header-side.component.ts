import { Component, OnInit, EventEmitter, Input, ViewChildren  , Output, Renderer2 } from '@angular/core';
import { ThemeService } from '../../services/theme.service';
import { LayoutService } from '../../services/layout.service';
import { TranslateService } from '@ngx-translate/core';
import { JwtAuthService } from '../../services/auth/jwt-auth.service';
import { EgretNotifications2Component } from '../egret-notifications2/egret-notifications2.component';
import { SessionsService } from 'app/views/sessions/sessions.service';
import { BehaviorSubject, Observable } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header-side',
  templateUrl: './header-side.template.html'
})
export class HeaderSideComponent implements OnInit {
  isLoginSubject = new BehaviorSubject<boolean>(SessionsService.hasToken());
  isLoggedIn: Observable<any> = new BehaviorSubject<boolean>(false);
  @Input() notificPanel;
  @ViewChildren(EgretNotifications2Component) noti;
  public availableLangs = [{
    name: 'EN',
    code: 'en',
    flag: 'us'
  }, {
    name: 'ES',
    code: 'es',
    flag: 'es'
  }];
  currentLang = this.availableLangs[0];

  public egretThemes;
  public layoutConf: any;
  constructor(
    private themeService: ThemeService,
    private layout: LayoutService,
    public translate: TranslateService,
    private renderer: Renderer2,
    public jwtAuth: JwtAuthService,
    private _router : Router
  ) {}
  ngOnInit() {
    this.egretThemes = this.themeService.egretThemes;
    this.layoutConf = this.layout.layoutConf;
    this.translate.use(this.currentLang.code);
  }
  setLang(lng) {
    this.currentLang = lng;
    this.translate.use(lng.code);
  }
  changeTheme(theme) {
    // this.themeService.changeTheme(theme);
  }
  toggleNotific() {
    this.notificPanel.toggle();
  }
  toggleSidenav() {
    if (this.layoutConf.sidebarStyle === 'closed') {
      return this.layout.publishLayoutChange({
        sidebarStyle: 'full'
      });
    }
    this.layout.publishLayoutChange({
      sidebarStyle: 'closed'
    });
  }

  toggleCollapse() {
    // compact --> full
    if (this.layoutConf.sidebarStyle === 'compact') {
      return this.layout.publishLayoutChange({
        sidebarStyle: 'full',
        sidebarCompactToggle: false
      }, {transitionClass: true});
    }

    // * --> compact
    this.layout.publishLayoutChange({
      sidebarStyle: 'compact',
      sidebarCompactToggle: true
    }, {transitionClass: true});

  }

  onSearch(e) {
    //   console.log(e)
  }
  signout() {
    localStorage.removeItem('userToken');
    localStorage.removeItem('user');
    this.isLoginSubject.next(false);
    this._router.navigateByUrl("/sessions/signin");
}
}
