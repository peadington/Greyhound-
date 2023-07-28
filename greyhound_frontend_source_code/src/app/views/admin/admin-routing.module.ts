import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DemocComponent } from './democ/democ.component';
import { GraphDataComponent } from './greyhound/graph-data/graph-data.component';
import { GraphTrainerDataComponent } from './trainer/graph-data/graph-data.component';

const routes: Routes = [
  { path: 'home', loadChildren: () => import('./dashboard/dashboard.module').then(m => m.DashboardModule) },
  { path: 'greyhound', loadChildren: () => import('./greyhound/greyhound.module').then(m => m.GreyhoundModule) },
  { path: 'trainer', loadChildren: () => import('./trainer/trainer.module').then(m => m.TrainerModule) },
  { path: 'compare', loadChildren: () => import('./compare/compare.module').then(m => m.CompareModule) },
  { path: 'report', loadChildren: () => import('./report/report.module').then(m => m.ReportModule) },
  { path: 'graphDetail/:id/:name', component: GraphDataComponent },
  { path: 'graph/:id/:name', component: GraphTrainerDataComponent },
  { path: 'greyhound-compare', loadChildren: () => import('./greyhound-compare/greyhound-compare.module').then(m => m.GreyhoundCompareModule) },
  { path: 'trainer-compare', loadChildren: () => import('./trainer-compare/trainer-compare.module').then(m => m.TrainerCompareModule) }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
