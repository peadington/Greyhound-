import { Component, OnInit, OnDestroy, AfterViewInit } from "@angular/core";
import { NavigationService } from "../../../shared/services/navigation.service";
import { ThemeService } from "../../services/theme.service";
import { BehaviorSubject, Observable, Subscription } from "rxjs";
import { ILayoutConf, LayoutService } from "app/shared/services/layout.service";
import { JwtAuthService } from "app/shared/services/auth/jwt-auth.service";
import { SessionsService } from "app/views/sessions/sessions.service";
import { Router } from "@angular/router";

@Component({
  selector: "app-sidebar-side",
  templateUrl: "./sidebar-side.component.html",
  styleUrls: ['./sidebar-side.component.scss']
})
export class SidebarSideComponent implements OnInit, OnDestroy, AfterViewInit {
  isLoginSubject = new BehaviorSubject<boolean>(SessionsService.hasToken());
  isLoggedIn: Observable<any> = new BehaviorSubject<boolean>(false);
  public menuItems: any[];
  public hasIconTypeMenuItem: boolean;
  public iconTypeMenuTitle: string;
  private menuItemsSub: Subscription;
  public layoutConf: ILayoutConf;
  userName: any;

  constructor(
    private navService: NavigationService,
    public themeService: ThemeService,
    private layout: LayoutService,
    public jwtAuth: JwtAuthService,
    private _router: Router
  ) {

    this.userName = JSON.parse(localStorage.getItem('user'))
  }

  ngOnInit() {
    // this.iconTypeMenuTitle = this.navService.iconTypeMenuTitle;
    this.menuItemsSub = this.navService.menuItems$.subscribe(menuItem => {
      this.menuItems = menuItem;
      //Checks item list has any icon type.
      this.hasIconTypeMenuItem = !!this.menuItems.filter(
        item => item.type === "icon"
      ).length;
    });
    this.layoutConf = this.layout.layoutConf;
  }
  ngAfterViewInit() { }
  ngOnDestroy() {
    if (this.menuItemsSub) {
      this.menuItemsSub.unsubscribe();
    }
  }
  toggleCollapse() {
    if (
      this.layoutConf.sidebarCompactToggle
    ) {
      this.layout.publishLayoutChange({
        sidebarCompactToggle: false
      });
    } else {
      this.layout.publishLayoutChange({
        // sidebarStyle: "compact",
        sidebarCompactToggle: true
      });
    }
  }

  signOut() {
    sessionStorage.removeItem("greyhound");
    sessionStorage.removeItem("trainer");
    localStorage.removeItem('userToken');
    localStorage.removeItem('user');
    this.isLoginSubject.next(false);
    this._router.navigateByUrl("/sessions/signin");
  }
}
