import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { GreyhoundCompareRoutingModule } from './greyhound-compare-routing.module';
import { GreyhoundCompareComponent } from './greyhound-compare.component';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatTableModule } from '@angular/material/table';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { NgChartsModule } from 'ng2-charts';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { NgApexchartsModule } from 'ng-apexcharts';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';

@NgModule({
  declarations: [
    GreyhoundCompareComponent
  ],
  imports: [
    CommonModule,
    GreyhoundCompareRoutingModule,
    MatToolbarModule,
    MatFormFieldModule,
    MatTableModule,
    ReactiveFormsModule,
    FormsModule,
    MatSelectModule,
    MatButtonModule,
    MatInputModule,
    MatProgressSpinnerModule,
    MatPaginatorModule,
    MatIconModule,
    MatButtonToggleModule,
    NgChartsModule,
    DragDropModule,
    NgApexchartsModule,
    MatDatepickerModule,
    MatNativeDateModule
  ]
})
export class GreyhoundCompareModule { }
