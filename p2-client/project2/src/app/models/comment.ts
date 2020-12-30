import { Answer } from "./answer";
import { Question } from "./question";
import { User } from "./user";

export class Comment {
    commentID : number;
    user : User;
    question: Question;
    answer? : Answer;
    details : string;
}
