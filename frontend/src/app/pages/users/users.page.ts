import { Component, ChangeDetectionStrategy, inject } from '@angular/core';
import {
  IonHeader,
  IonToolbar,
  IonTitle,
  IonButtons,
  IonButton,
  IonIcon,
  IonContent,
  IonRefresher,
  IonRefresherContent,
  IonInfiniteScroll,
  IonInfiniteScrollContent,
  IonCard,
  IonCardContent,
  ModalController,
  AlertController,
  ToastController
} from '@ionic/angular/standalone';
import { UserService } from '../../services/user.service';
import { UserListComponent } from '../../components/user-list/user-list.component';
import { UserSearchComponent } from '../../components/user-search/user-search.component';
import { UserFormComponent } from '../../components/user-form/user-form.component';
import { User, CreateUserRequest, UpdateUserRequest } from '../../models/user.model';
import { addIcons } from 'ionicons';
import { addOutline } from 'ionicons/icons';

@Component({
  selector: 'app-users',
  imports: [
    IonHeader,
    IonToolbar,
    IonTitle,
    IonButtons,
    IonButton,
    IonIcon,
    IonContent,
    IonRefresher,
    IonRefresherContent,
    IonInfiniteScroll,
    IonInfiniteScrollContent,
    IonCard,
    IonCardContent,
    UserListComponent,
    UserSearchComponent
  ],
  templateUrl: './users.page.html',
  styleUrl: './users.page.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class UsersPage {
  userService = inject(UserService);
  private modalController = inject(ModalController);
  private alertController = inject(AlertController);
  private toastController = inject(ToastController);

  private currentSearchQuery = '';
  private currentFilter: 'all' | 'active' | 'inactive' = 'all';

  constructor() {
    addIcons({ addOutline });
    this.loadUsers();
  }

  loadUsers(refresh = false) {
    if (refresh) {
      this.userService.reset();
    }

    if (this.currentSearchQuery || this.currentFilter !== 'all') {
      this.userService.searchUsers(this.currentSearchQuery, this.currentFilter);
    } else {
      this.userService.getUsers(0, 20);
    }
  }

  loadMoreUsers() {
    if (this.userService.hasMore() && !this.userService.loading()) {
      this.userService.loadNextPage();
    }
  }

  handleSearch(query: string) {
    this.currentSearchQuery = query;
    this.loadUsers(true);
  }

  handleFilter(status: 'all' | 'active' | 'inactive') {
    this.currentFilter = status;
    this.loadUsers(true);
  }

  async handleRefresh(event: any) {
    this.loadUsers(true);

    // Wait for loading to complete
    const checkLoading = setInterval(() => {
      if (!this.userService.loading()) {
        clearInterval(checkLoading);
        event.target.complete();
      }
    }, 100);
  }

  async onInfiniteScroll(event: any) {
    this.loadMoreUsers();

    // Wait for loading to complete
    const checkLoading = setInterval(() => {
      if (!this.userService.loading()) {
        clearInterval(checkLoading);
        event.target.complete();
      }
    }, 100);
  }

  async openCreateModal() {
    const modal = await this.modalController.create({
      component: UserFormComponent,
      componentProps: {
        user: null
      }
    });

    await modal.present();

    const { data, role } = await modal.onDidDismiss();
    if (role === 'submit' && data) {
      this.createUser(data);
    }
  }

  async openEditModal(user: User) {
    const modal = await this.modalController.create({
      component: UserFormComponent,
      componentProps: {
        user: user
      }
    });

    await modal.present();

    const { data, role } = await modal.onDidDismiss();
    if (role === 'submit' && data) {
      this.updateUser(user.id, data);
    }
  }

  private createUser(request: CreateUserRequest) {
    this.userService.createUser(request).subscribe({
      next: async () => {
        await this.showToast('User created successfully', 'success');
        this.loadUsers(true);
      },
      error: async (error) => {
        await this.showToast(error.message || 'Failed to create user', 'danger');
      }
    });
  }

  private updateUser(id: number, request: UpdateUserRequest) {
    this.userService.updateUser(id, request).subscribe({
      next: async () => {
        await this.showToast('User updated successfully', 'success');
        this.loadUsers(true);
      },
      error: async (error) => {
        await this.showToast(error.message || 'Failed to update user', 'danger');
      }
    });
  }

  async handleDeleteUser(user: User) {
    const alert = await this.alertController.create({
      header: 'Confirm Delete',
      message: `Are you sure you want to delete ${user.firstName} ${user.lastName}?`,
      buttons: [
        {
          text: 'Cancel',
          role: 'cancel'
        },
        {
          text: 'Delete',
          role: 'confirm',
          handler: () => {
            this.deleteUser(user.id);
          }
        }
      ]
    });

    await alert.present();
  }

  private deleteUser(id: number) {
    this.userService.deleteUser(id).subscribe({
      next: async () => {
        await this.showToast('User deleted successfully', 'success');
        this.loadUsers(true);
      },
      error: async (error) => {
        await this.showToast(error.message || 'Failed to delete user', 'danger');
      }
    });
  }

  private async showToast(message: string, color: string) {
    const toast = await this.toastController.create({
      message,
      duration: 3000,
      color,
      position: 'bottom'
    });
    await toast.present();
  }
}
