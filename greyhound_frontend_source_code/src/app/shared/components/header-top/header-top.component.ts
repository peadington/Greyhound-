import { Component, OnInit, Input, OnDestroy, Renderer2 } from '@angular/core';
import { NavigationService } from "../../../shared/services/navigation.service";
import { BehaviorSubject, Observable, Subscription } from 'rxjs';
import { ThemeService } from '../../../shared/services/theme.service';
import { TranslateService } from '@ngx-translate/core';
import { LayoutService } from '../../services/layout.service';
import { JwtAuthService } from 'app/shared/services/auth/jwt-auth.service';
import { SessionsService } from 'app/views/sessions/sessions.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header-top',
  templateUrl: './header-top.component.html'
})
export class HeaderTopComponent implements OnInit, OnDestroy {
  isLoginSubject = new BehaviorSubject<boolean>(SessionsService.hasToken());
  isLoggedIn: Observable<any> = new BehaviorSubject<boolean>(false);
  layoutConf: any;
  menuItems: any;
  menuItemSub: Subscription;
  egretThemes: any[] = [];
  currentLang = 'en';
  availableLangs = [{
    name: 'English',
    code: 'en',
  }, {
    name: 'Spanish',
    code: 'es',
  }]
  @Input() notificPanel;
  constructor(
    private layout: LayoutService,
    private navService: NavigationService,
    public themeService: ThemeService,
    public translate: TranslateService,
    private renderer: Renderer2,
    public jwtAuth: JwtAuthService,
    private _router: Router
  ) { }

  ngOnInit() {
    this.layoutConf = this.layout.layoutConf;
    this.egretThemes = this.themeService.egretThemes;
    this.menuItemSub = this.navService.menuItems$
      .subscribe(res => {
        res = res.filter(item => item.type !== 'icon' && item.type !== 'separator');
        let limit = 4
        let mainItems: any[] = res.slice(0, limit)
        if (res.length <= limit) {
          return this.menuItems = mainItems
        }
        let subItems: any[] = res.slice(limit, res.length - 1)
        mainItems.push({
          name: 'More',
          type: 'dropDown',
          tooltip: 'More',
          icon: 'more_horiz',
          sub: subItems
        })
        this.menuItems = mainItems
      })
  }
  ngOnDestroy() {
    this.menuItemSub.unsubscribe()
  }
  setLang() {
    this.translate.use(this.currentLang)
  }
  changeTheme(theme) {
    this.layout.publishLayoutChange({ matTheme: theme.name })
  }
  toggleNotific() {
    this.notificPanel.toggle();
  }
  toggleSidenav() {
    if (this.layoutConf.sidebarStyle === 'closed') {
      return this.layout.publishLayoutChange({
        sidebarStyle: 'full'
      })
    }
    this.layout.publishLayoutChange({
      sidebarStyle: 'closed'
    })
  }

  signout() {
    sessionStorage.removeItem("greyhound");
    sessionStorage.removeItem("trainer");
    localStorage.removeItem('userToken');
    localStorage.removeItem('user');
    this.isLoginSubject.next(false);
    this._router.navigateByUrl("/sessions/signin");
  }

}
