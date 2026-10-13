import type { ProblemDetail } from '../types/api';

const BASE_URL = '/api';

export class ApiError extends Error {
    status: number;
    problemDetail: ProblemDetail | null;

    constructor(status: number, problemDetail: ProblemDetail | null) {
        super(problemDetail?.detail ?? `HTTP ${status}`);
        this.status = status;
        this.problemDetail = problemDetail;
    }
}

export function apiFetch<T>(
    path: string,
    options: RequestInit = {},
): Promise<T> {
    return fetch(`${BASE_URL}${path}`, {
        headers: { 'Content-Type': 'application/json', ...options.headers },
        ...options,
    }).then((response) => {
        if (!response.ok) {
            return response
                .json()
                .catch(() => null)
                .then((problemDetail: ProblemDetail | null) => {
                    throw new ApiError(response.status, problemDetail);
                });
        }

        if (response.status === 204) {
            return null as T;
        }

        return response.json() as Promise<T>;
    });
}
