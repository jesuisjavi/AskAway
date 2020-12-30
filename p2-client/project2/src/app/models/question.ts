import { Answer } from "./answer";
import { Comment } from "./comment";
import { Skill } from "./skill";
import { User } from "./user";

export class Question {
    public questionID: number;
    public user: User;
    public title: string;
    public details: string;
    public datePosted?: string;
    public dateResolved?: string;
    public skills?: Array<Skill>;
    public answers?: Array<Answer>;
    public comments?: Array<Comment>;
}
