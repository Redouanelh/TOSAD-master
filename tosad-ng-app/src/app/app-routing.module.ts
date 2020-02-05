import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { MainComponent } from './main/main.component';
import { ManageComponent } from './manage/manage.component';
import { LoginComponent } from './login/login.component';

const routes: Routes = [
  { path: 'define', component: MainComponent },
  { path: 'manage', component: ManageComponent},
  { path: '', component: LoginComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
