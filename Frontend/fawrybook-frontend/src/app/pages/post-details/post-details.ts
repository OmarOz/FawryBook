import { Component} from '@angular/core';
import { PostDetailsComponent } from '../../shared/components/post-details-component/post-details-component';


@Component({
  selector: 'app-post-details',
  standalone: true,
  imports: [PostDetailsComponent],
  templateUrl: './post-details.html',
  styleUrl: './post-details.css',
})
export class PostDetailComponent{

}
