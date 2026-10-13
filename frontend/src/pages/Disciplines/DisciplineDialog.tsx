import { useEffect, useState } from 'react';
import type { FormEvent } from 'react';
import {
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    TextField,
    Button,
    Alert,
} from '@mui/material';
import { ApiError } from '../../api/client';
import { createDiscipline, updateDiscipline } from '../../api/disciplines';
import type { Discipline } from '../../types/discipline';
import styles from './DisciplineDialog.module.css';

interface DisciplineDialogProps {
    open: boolean;
    discipline: Discipline | null;
    onClose: () => void;
    onSaved: (saved: Discipline) => void;
}

function DisciplineDialog({
    open,
    discipline,
    onClose,
    onSaved,
}: DisciplineDialogProps) {
    const [name, setName] = useState('');
    const [fieldError, setFieldError] = useState<string | undefined>();
    const [submitError, setSubmitError] = useState<string | null>(null);
    const [submitting, setSubmitting] = useState(false);

    useEffect(() => {
        if (!open) return;
        setName(discipline?.name ?? '');
        setFieldError(undefined);
        setSubmitError(null);
    }, [open, discipline]);

    function handleSubmit(e: FormEvent) {
        e.preventDefault();
        setSubmitError(null);
        setFieldError(undefined);
        setSubmitting(true);

        const dto = { name };
        const request = discipline
            ? updateDiscipline(discipline.id, dto)
            : createDiscipline(dto);

        request
            .then((saved) => {
                onSaved(saved);
                onClose();
            })
            .catch((err: unknown) => {
                if (err instanceof ApiError && err.status === 400) {
                    const errors = err.problemDetail?.errors ?? [];
                    const nameError = errors.find((fe) => fe.field === 'name');
                    setFieldError(nameError?.message);
                } else if (err instanceof ApiError) {
                    setSubmitError(err.problemDetail?.detail ?? err.message);
                } else {
                    setSubmitError('Неизвестная ошибка');
                }
            })
            .finally(() => setSubmitting(false));
    }

    return (
        <Dialog open={open} onClose={onClose} maxWidth="xs" fullWidth>
            <DialogTitle>
                {discipline
                    ? `Редактировать ${discipline.name}`
                    : 'Новая Discipline'}
            </DialogTitle>
            <form onSubmit={handleSubmit}>
                <DialogContent className={styles.content}>
                    {submitError && (
                        <Alert severity="error">{submitError}</Alert>
                    )}
                    <TextField
                        label="Имя"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        error={Boolean(fieldError)}
                        helperText={fieldError}
                        fullWidth
                        required
                        autoFocus
                    />
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

export default DisciplineDialog;
