import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatLegacyCardModule as MatCardModule } from '@angular/material/legacy-card';
import { MatIconModule } from '@angular/material/icon';
import { MatLegacyListModule as MatListModule } from '@angular/material/legacy-list';
import { FlexLayoutModule } from '@angular/flex-layout';
import { DragulaModule } from 'ng2-dragula';

import { DragndropComponent } from './dragndrop.component';
import { DragndropRoutes } from "./dragndrop.routing";

@NgModule({
  imports: [
    CommonModule,
    MatIconModule,
    MatCardModule,
    MatListModule,
    FlexLayoutModule,
    DragulaModule.forRoot(),
    RouterModule.forChild(DragndropRoutes)
  ],
  declarations: [DragndropComponent]
})
export class DragndropModule { }
