export class AddComment {
    constructor(public userID:string='',
                public questionID:string = '',
                public answerID:string = '',
                public details: string = '') {
    }
  }
