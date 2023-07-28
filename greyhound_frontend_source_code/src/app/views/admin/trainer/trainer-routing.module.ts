import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { GraphTrainerDataComponent } from './graph-data/graph-data.component';
import { TrainerComponent } from './trainer.component';

const routes: Routes = [
  { path: '', component: TrainerComponent },
  { path: 'graph/:trainer/:track', component: GraphTrainerDataComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TrainerRoutingModule { }
