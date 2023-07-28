import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { GreyhoundCompareComponent } from './greyhound-compare.component';

const routes: Routes = [{ path: '', component: GreyhoundCompareComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class GreyhoundCompareRoutingModule { }
