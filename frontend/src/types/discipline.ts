export interface LabWorkRef {
    id: number;
    name: string;
}

export interface Discipline {
    id: number;
    name: string;
    labsCount: number;
    labWorks: LabWorkRef[];
}

export interface DisciplineRequestDto {
    name: string;
}
