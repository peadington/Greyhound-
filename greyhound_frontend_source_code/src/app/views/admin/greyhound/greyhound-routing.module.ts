import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { GraphComponent } from './graph/graph.component';
import { GreyhoundComponent } from './greyhound.component';

const routes: Routes = [
  { path: '', component: GreyhoundComponent },
  { path: 'graph', component: GraphComponent },
  { path: 'id/:greyhoundId', component: GreyhoundComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class GreyhoundRoutingModule { }
