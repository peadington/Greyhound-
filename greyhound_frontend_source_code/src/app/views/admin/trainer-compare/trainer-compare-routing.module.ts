import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TrainerCompareComponent } from './trainer-compare.component';

const routes: Routes = [{ path: '', component: TrainerCompareComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TrainerCompareRoutingModule { }
