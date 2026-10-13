import { useEffect, useState } from 'react';
import type { FormEvent } from 'react';
import {
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    TextField,
    Button,
    MenuItem,
    Alert,
} from '@mui/material';
import { ApiError } from '../../api/client';
import { createLabWork, updateLabWork } from '../../api/labworks';
import { fetchDisciplines } from '../../api/disciplines';
import type { Difficulty, DisciplineRef, LabWork } from '../../types/labwork';
import { DIFFICULTIES } from '../../types/labwork';
import styles from './LabWorkDialog.module.css';

interface FormState {
    name: string;
    x: string;
    y: string;
    description: string;
    difficulty: Difficulty | '';
    disciplineId: string;
    minimalPoint: string;
    personalQualitiesMaximum: string;
    tunedInWork: string;
}

const EMPTY_FORM: FormState = {
    name: '',
    x: '',
    y: '',
    description: '',
    difficulty: '',
    disciplineId: '',
    minimalPoint: '',
    personalQualitiesMaximum: '',
    tunedInWork: '0',
};

function formFromLabWork(labWork: LabWork): FormState {
    return {
        name: labWork.name,
        x: String(labWork.coordinates.x),
        y: String(labWork.coordinates.y),
        description: labWork.description,
        difficulty: labWork.difficulty ?? '',
        disciplineId: String(labWork.discipline.id),
        minimalPoint: String(labWork.minimalPoint),
        personalQualitiesMaximum: String(labWork.personalQualitiesMaximum),
        tunedInWork: String(labWork.tunedInWork),
    };
}

interface LabWorkDialogProps {
    open: boolean;
    labWork: LabWork | null;
    onClose: () => void;
    onSaved: (saved: LabWork) => void;
}

function LabWorkDialog({
    open,
    labWork,
    onClose,
    onSaved,
}: LabWorkDialogProps) {
    const [form, setForm] = useState<FormState>(EMPTY_FORM);
    const [disciplines, setDisciplines] = useState<DisciplineRef[]>([]);
    const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({});
    const [submitError, setSubmitError] = useState<string | null>(null);
    const [submitting, setSubmitting] = useState(false);

    useEffect(() => {
        if (!open) return;
        setForm(labWork ? formFromLabWork(labWork) : EMPTY_FORM);
        setFieldErrors({});
        setSubmitError(null);
    }, [open, labWork]);

    useEffect(() => {
        if (!open) return;
        fetchDisciplines()
            .then(setDisciplines)
            .catch(() => setDisciplines([]));
    }, [open]);

    function setField<K extends keyof FormState>(key: K, value: FormState[K]) {
        setForm((prev) => ({ ...prev, [key]: value }));
    }

    function fieldError(name: string): string | undefined {
        return fieldErrors[name];
    }

    function handleSubmit(e: FormEvent) {
        e.preventDefault();
        setSubmitError(null);
        setFieldErrors({});
        setSubmitting(true);

        const dto = {
            name: form.name,
            coordinates: { x: Number(form.x), y: Number(form.y) },
            description: form.description,
            difficulty: form.difficulty === '' ? null : form.difficulty,
            minimalPoint: Number(form.minimalPoint),
            personalQualitiesMaximum: Number(form.personalQualitiesMaximum),
            tunedInWork: Number(form.tunedInWork),
            disciplineId: Number(form.disciplineId),
        };

        const request = labWork
            ? updateLabWork(labWork.id, dto)
            : createLabWork(dto);

        request
            .then((saved) => {
                onSaved(saved);
                onClose();
            })
            .catch((err: unknown) => {
                if (err instanceof ApiError && err.status === 400) {
                    const errors = err.problemDetail?.errors ?? [];
                    const map: Record<string, string> = {};
                    errors.forEach((fe) => {
                        map[fe.field] = fe.message;
                    });
                    setFieldErrors(map);
                } else if (err instanceof ApiError) {
                    setSubmitError(err.problemDetail?.detail ?? err.message);
                } else {
                    setSubmitError('Неизвестная ошибка');
                }
            })
            .finally(() => setSubmitting(false));
    }

    return (
        <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
            <DialogTitle>
                {labWork ? `Редактировать ${labWork.name}` : 'Новый LabWork'}
            </DialogTitle>
            <form onSubmit={handleSubmit}>
                <DialogContent>
                    <div className={styles.form}>
                        {submitError && (
                            <Alert severity="error">{submitError}</Alert>
                        )}

                        <TextField
                            label="Имя"
                            value={form.name}
                            onChange={(e) => setField('name', e.target.value)}
                            error={Boolean(fieldError('name'))}
                            helperText={fieldError('name')}
                            fullWidth
                            required
                        />

                        <div className={styles.row}>
                            <TextField
                                className={styles.fieldNarrow}
                                label="Coordinates.x"
                                value={form.x}
                                onChange={(e) => setField('x', e.target.value)}
                                error={Boolean(fieldError('coordinates.x'))}
                                helperText={fieldError('coordinates.x')}
                                required
                            />
                            <TextField
                                className={styles.fieldNarrow}
                                label="Coordinates.y"
                                value={form.y}
                                onChange={(e) => setField('y', e.target.value)}
                                error={Boolean(fieldError('coordinates.y'))}
                                helperText={fieldError('coordinates.y')}
                                required
                            />
                        </div>

                        <TextField
                            label="Описание"
                            value={form.description}
                            onChange={(e) =>
                                setField('description', e.target.value)
                            }
                            error={Boolean(fieldError('description'))}
                            helperText={fieldError('description')}
                            fullWidth
                            multiline
                            minRows={2}
                            required
                        />

                        <div className={styles.row}>
                            <TextField
                                className={styles.field}
                                label="Difficulty"
                                select
                                value={form.difficulty}
                                onChange={(e) =>
                                    setField(
                                        'difficulty',
                                        e.target.value as Difficulty | '',
                                    )
                                }
                                error={Boolean(fieldError('difficulty'))}
                                helperText={fieldError('difficulty')}
                            >
                                <MenuItem value="">—</MenuItem>
                                {DIFFICULTIES.map((d) => (
                                    <MenuItem key={d} value={d}>
                                        {d}
                                    </MenuItem>
                                ))}
                            </TextField>

                            <TextField
                                className={styles.field}
                                label="Discipline"
                                select
                                value={form.disciplineId}
                                onChange={(e) =>
                                    setField('disciplineId', e.target.value)
                                }
                                error={Boolean(fieldError('disciplineId'))}
                                helperText={fieldError('disciplineId')}
                                required
                            >
                                {disciplines.map((d) => (
                                    <MenuItem key={d.id} value={String(d.id)}>
                                        {d.name}
                                    </MenuItem>
                                ))}
                            </TextField>
                        </div>

                        <div className={styles.row}>
                            <TextField
                                className={styles.fieldNarrow}
                                label="Min. point"
                                value={form.minimalPoint}
                                onChange={(e) =>
                                    setField('minimalPoint', e.target.value)
                                }
                                error={Boolean(fieldError('minimalPoint'))}
                                helperText={fieldError('minimalPoint')}
                                required
                            />
                            <TextField
                                className={styles.fieldNarrow}
                                label="Pers. qual. max"
                                value={form.personalQualitiesMaximum}
                                onChange={(e) =>
                                    setField(
                                        'personalQualitiesMaximum',
                                        e.target.value,
                                    )
                                }
                                error={Boolean(
                                    fieldError('personalQualitiesMaximum'),
                                )}
                                helperText={fieldError(
                                    'personalQualitiesMaximum',
                                )}
                                required
                            />
                            <TextField
                                className={styles.fieldNarrow}
                                label="Tuned in"
                                value={form.tunedInWork}
                                onChange={(e) =>
                                    setField('tunedInWork', e.target.value)
                                }
                                error={Boolean(fieldError('tunedInWork'))}
                                helperText={fieldError('tunedInWork')}
                                required
                            />
                        </div>
                    </div>
                </DialogContent>
                <DialogActions>
                    <Button onClick={onClose} disabled={submitting}>
                        Отмена
                    </Button>
                    <Button
                        type="submit"
                        variant="contained"
                        disabled={submitting}
                    >
                        Сохранить
                    </Button>
                </DialogActions>
            </form>
        </Dialog>
    );
}

export default LabWorkDialog;
