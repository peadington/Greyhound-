import { Injectable } from '@angular/core';
import { formatDate } from '@angular/common';
import { environment } from '../../../environments/environment.prod';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Pagination } from '../interfaces/pagination.interface';
import * as FileSaver from 'file-saver';
import * as XLSX from 'xlsx';
const EXCEL_TYPE = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8';
const EXCEL_EXTENSION = '.xlsx';

@Injectable({
    providedIn: 'root'
})  
export class UtilityService {

    apiUrl: string = environment.apiUrl;
    pagination: Pagination = { totalPages: 0, totalCount: 0, currentPage: 1, perPage: 25, data: [] };
    PAGINATION_OPTIONS: any[] = [10, 25, 50, 100];
    constructor(
        private _matSnackBar: MatSnackBar
    ) { }

    /**
     * Validate Email
     * 
     * @param email 
     * 
     * @returns {Boolean}
     */
    validateEmail(email: string): boolean {
        var x = email;
        var atposition = x.indexOf("@");
        var dotposition = x.lastIndexOf(".");
        if (atposition < 1 || dotposition < atposition + 2
            || dotposition + 2 >= x.length) {
            // this.openMatSnackBar('Please enter a valid e-mail address ', 'Try Again');
            return false;
        } else {
            return true;
        }
    }

    /**
     * Get Formated Date Time
     * 
     * @param date 
     * 
     * @returns formated date
     */
    getFormatedDateTime(date: any): any {
        return date ? formatDate(date, 'MMM d, yyyy, HH:mm', 'en-US', '+0530') : '';
    }

    /**
     * Get Formated Date
     * 
     * @param date
     * 
     * @returns formated date 
     */
    getFormatedDate(date: any): string {
        return date ? formatDate(date, 'MMM d,yyyy', 'en-US', '+0530') : '';
    }

    /**
     * Get Download File Url
     * 
     * @param file 
     * 
     * @returns file url
     */
    getDownloadFileUrl(file: any): string {
        let url = this.apiUrl + 'file/downloadFile/';
        if (file && file !== '') {
            url += file;
        }
        return url;
    }

    /**
     * Get File Url
     * 
     * @param file 
     * @param type 
     */
    getFileUrl(file: any, type?: any): any {
        let url = this.apiUrl + 'file/get/';
        if (file && file !== '') {
            url += file;
        }
        return url;
    }

    /**
     * Success toastr
     * 
     * @param message 
     * @param status 
     */
    successMessage(message: string, status: any) {
        this._matSnackBar.open(message, status, {
            duration: 4000,
            verticalPosition: 'top',
            horizontalPosition: 'center',
        });
    }

    /**
    * Error toastr
    * 
    * @param message 
    * @param status 
    */
    errorMessage(message: string, status: any) {
        this._matSnackBar.open(message, status, {
            duration: 4000,
            verticalPosition: 'top',
            horizontalPosition: 'center',
        });
    }

    /**
     * Get session user
     */
    getSessionUser(): any {
        let user = localStorage.getItem("user");
        return user ? JSON.parse(user) : null;
    }

    exportToExcel(excelData: any, excelFileName: any) {
        const worksheet: XLSX.WorkSheet = XLSX.utils.json_to_sheet(excelData);
        // console.log('worksheet', worksheet);
        const workbook: XLSX.WorkBook = { Sheets: { 'data': worksheet }, SheetNames: ['data'] };
        const excelBuffer: any = XLSX.write(workbook, { bookType: 'xlsx', type: 'array' });
        //const excelBuffer: any = XLSX.write(workbook, { bookType: 'xlsx', type: 'buffer' });
        this.saveAsExcelFile(excelBuffer, excelFileName);
    }
    
    private saveAsExcelFile(buffer: any, fileName: string): void {
      const data: Blob = new Blob([buffer], {
          type: EXCEL_TYPE
      });
      FileSaver.saveAs(data, fileName + '_' + new Date().getTime() + EXCEL_EXTENSION);
    }
}
