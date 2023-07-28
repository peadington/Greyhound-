import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MatIconModule } from "@angular/material/icon";
import { MatToolbarModule } from "@angular/material/toolbar";
import { MatMenuModule } from "@angular/material/menu";
import { MatButtonModule } from "@angular/material/button";
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';

import { GreyhoundComponent } from './greyhound.component';
import { GreyhoundRoutingModule } from './greyhound-routing.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatTableModule } from "@angular/material/table";
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { GraphDataComponent } from './graph-data/graph-data.component';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { NgChartsModule } from 'ng2-charts';
import { RaceDataComponent } from './race-data/race-data.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { GraphComponent } from './graph/graph.component';

import * as CanvasJSAngularChart from '../../../../assets/canvasjs.angular.component';
import { NgApexchartsModule } from 'ng-apexcharts';
var CanvasJSChart = CanvasJSAngularChart.CanvasJSChart;

@NgModule({
  declarations: [
    GreyhoundComponent,
    GraphDataComponent,
    RaceDataComponent,
    GraphComponent,
    CanvasJSChart
  ],
  imports: [
    CommonModule,
    GreyhoundRoutingModule,
    MatToolbarModule,
    MatFormFieldModule,
    MatSelectModule,
    ReactiveFormsModule,
    FormsModule,
    MatInputModule,
    MatTableModule,
    MatProgressSpinnerModule,
    MatButtonModule,
    DragDropModule,
    NgChartsModule,
    MatIconModule,
    MatDialogModule,
    MatPaginatorModule,
    MatMenuModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatAutocompleteModule,
    NgApexchartsModule

  ]
})
export class GreyhoundModule { }
