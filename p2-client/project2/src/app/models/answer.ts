import { Question } from "./question";

export class Answer {
    answerID : number;
    question : Question;
    userId : number;
    details : string;
    isTopAnswer : boolean;
    datePosted : string;
    comments:Array<Comment>;
}
