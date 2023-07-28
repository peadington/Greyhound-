import { Injectable } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';
import { ApiService } from 'app/shared/services/api.service';
import { NavigationService } from 'app/shared/services/navigation.service';
import { UtilityService } from 'app/shared/services/utility.service';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class SessionsService {

    isLoginSubject = new BehaviorSubject<boolean>(SessionsService.hasToken());
    isLoggedIn: Observable<any> = new BehaviorSubject<boolean>(false);

    constructor(
        private _formBuilder: FormBuilder,
        private _apiService: ApiService,
        private _router: Router,
        public _jwtHelper: JwtHelperService,
        private _utilityService: UtilityService,
        private _navigationService: NavigationService
    ) { }

    /**
     * Craete Login Form
     */
    createForm(): FormGroup {
        return this._formBuilder.group({
            username: [null, Validators.required],
            password: [null, Validators.required]
        })
    }

    /**
     * Login User
     * 
     * @param data 
     */
    login(data: any) {
        this._apiService.post(data, 'auth/login').then((response: any) => {
            if (response && response.body.status === 'OK') {
                localStorage.setItem('userToken', response.body.data.token);
                let user = {
                    id: response.body.data.user_details._id,
                    email: response.body.data.user_details.email,
                    name: response.body.data.user_details.name,
                    role: response.body.data.user_details.role,
                    profileImage: response.body.data.user_details.profileImage,
                    permissions: response.body.data.user_details.permissions
                };
                localStorage.setItem('user', JSON.stringify(user));
                this.isLoginSubject.next(true);
                // this._navigationService.setNavigation();
                this._router.navigate(['/home']);
            } else {
                this._utilityService.errorMessage(response.body.message, response.statusText);
            }
        }, error => {
            if (error.status !== 0)
                this._utilityService.errorMessage(error.error.message, error.statusText);
            else
                this._utilityService.errorMessage("Server Error", "Try Again");
        })
    }

    /**
     * Method For has token
     */
    public static hasToken(): boolean {
        return !!localStorage.getItem('userToken');
    }

    /**
     * Log out the user then tell all the subscribers about the new status
     */
    logout(): void {
        sessionStorage.removeItem("greyhound");
        sessionStorage.removeItem("trainer");
        localStorage.removeItem('userToken');
        localStorage.removeItem('user');
        this.isLoginSubject.next(false);
        this._router.navigateByUrl("/sessions/signin");
    }

    public isAuthenticated(): boolean {
        const token: any = localStorage.getItem('userToken');
        //Check whether the token is expired and return
        //true or false
        return !this._jwtHelper.isTokenExpired(token);
    }

}
