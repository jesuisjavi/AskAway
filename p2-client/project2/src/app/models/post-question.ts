export class PostQuestion {
    constructor(public userID:string='',
                public title:string = '',
                public details:string = '',
                public tags: string[] = []) {
    }
  }