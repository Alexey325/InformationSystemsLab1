import { useEffect, useState } from 'react';
import {
    Paper,
    Typography,
    Button,
    TextField,
    MenuItem,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Alert,
} from '@mui/material';
import { ApiError } from '../api/client';
import {
    addHardestToDiscipline,
    deleteLabWork,
    fetchByMinimalPointGreaterThan,
    fetchGroupedByName,
    fetchLabWorks,
    fetchMaxTunedInWork,
} from '../api/labworks';
import { fetchDisciplines } from '../api/disciplines';
import type { LabWork, NameCount } from '../types/labwork';
import type { DisciplineRef } from '../types/labwork';
import ConfirmDialog from '../components/ConfirmDialog';
import styles from './SpecialOperationsPage.module.css';

function errorMessage(err: unknown): string {
    if (err instanceof ApiError)
        return err.problemDetail?.detail ?? err.message;
    if (err instanceof Error) return err.message;
    return 'Неизвестная ошибка';
}

function SpecialOperationsPage() {
    const [labWorks, setLabWorks] = useState<LabWork[]>([]);
    const [disciplines, setDisciplines] = useState<DisciplineRef[]>([]);

    useEffect(() => {
        fetchLabWorks({ page: 0, size: 200 })
            .then((page) => setLabWorks(page.content))
            .catch(() => setLabWorks([]));
        fetchDisciplines()
            .then(setDisciplines)
            .catch(() => setDisciplines([]));
    }, []);

    function reloadLabWorks() {
        fetchLabWorks({ page: 0, size: 200 })
            .then((page) => setLabWorks(page.content))
            .catch(() => setLabWorks([]));
    }

    // 1. Максимальный tunedInWork
    const [maxLoading, setMaxLoading] = useState(false);
    const [maxError, setMaxError] = useState<string | null>(null);
    const [maxResult, setMaxResult] = useState<LabWork | null>(null);

    function handleFindMax() {
        setMaxLoading(true);
        setMaxError(null);
        fetchMaxTunedInWork()
            .then(setMaxResult)
            .catch((err: unknown) => setMaxError(errorMessage(err)))
            .finally(() => setMaxLoading(false));
    }

    // 2. Группировка по name
    const [groupedLoading, setGroupedLoading] = useState(false);
    const [groupedError, setGroupedError] = useState<string | null>(null);
    const [groupedResult, setGroupedResult] = useState<NameCount[] | null>(
        null,
    );

    function handleShowGrouped() {
        setGroupedLoading(true);
        setGroupedError(null);
        fetchGroupedByName()
            .then(setGroupedResult)
            .catch((err: unknown) => setGroupedError(errorMessage(err)))
            .finally(() => setGroupedLoading(false));
    }

    // 3. minimalPoint больше заданного
    const [threshold, setThreshold] = useState('0');
    const [minPointLoading, setMinPointLoading] = useState(false);
    const [minPointError, setMinPointError] = useState<string | null>(null);
    const [minPointResult, setMinPointResult] = useState<LabWork[] | null>(
        null,
    );

    function handleFindByMinPoint() {
        setMinPointLoading(true);
        setMinPointError(null);
        fetchByMinimalPointGreaterThan(Number(threshold))
            .then(setMinPointResult)
            .catch((err: unknown) => setMinPointError(errorMessage(err)))
            .finally(() => setMinPointLoading(false));
    }

    // 4. Удалить LabWork из программы дисциплины
    const [deleteSelectedId, setDeleteSelectedId] = useState('');
    const [deleteConfirmOpen, setDeleteConfirmOpen] = useState(false);
    const [deleteError, setDeleteError] = useState<string | null>(null);
    const [deleteDone, setDeleteDone] = useState<string | null>(null);

    function handleConfirmDelete() {
        const id = Number(deleteSelectedId);
        setDeleteError(null);
        deleteLabWork(id)
            .then(() => {
                setDeleteDone(`LabWork #${id} удалён.`);
                setDeleteSelectedId('');
                reloadLabWorks();
            })
            .catch((err: unknown) => setDeleteError(errorMessage(err)))
            .finally(() => setDeleteConfirmOpen(false));
    }

    // 5. Добавить 3 самых сложных LabWork в дисциплину
    const [targetDisciplineId, setTargetDisciplineId] = useState('');
    const [addLoading, setAddLoading] = useState(false);
    const [addError, setAddError] = useState<string | null>(null);
    const [addResult, setAddResult] = useState<LabWork[] | null>(null);

    function handleAddHardest() {
        setAddLoading(true);
        setAddError(null);
        addHardestToDiscipline(Number(targetDisciplineId))
            .then((result) => {
                setAddResult(result);
                reloadLabWorks();
            })
            .catch((err: unknown) => setAddError(errorMessage(err)))
            .finally(() => setAddLoading(false));
    }

    const deleteTarget = labWorks.find(
        (lw) => String(lw.id) === deleteSelectedId,
    );

    return (
        <div className={styles.page}>
            <div className={styles.header}>
                <Typography variant="h5" className={styles.title}>
                    Спецоперации
                </Typography>
            </div>

            <div className={styles.grid}>
                <Paper className={styles.card}>
                    <Typography className={styles.cardTitle}>
                        Максимальный tunedInWork
                    </Typography>
                    <Typography
                        variant="body2"
                        className={styles.cardDescription}
                    >
                        Вернуть любой объект с наибольшим значением tunedInWork.
                    </Typography>
                    <Button
                        variant="contained"
                        onClick={handleFindMax}
                        disabled={maxLoading}
                        className={styles.cardButton}
                    >
                        Найти
                    </Button>
                    {maxError && <Alert severity="error">{maxError}</Alert>}
                    {maxResult && (
                        <div className={styles.result}>
                            {maxResult.name} (id {maxResult.id}) — tunedInWork:{' '}
                            {maxResult.tunedInWork}
                        </div>
                    )}
                </Paper>

                <Paper className={styles.card}>
                    <Typography className={styles.cardTitle}>
                        Группировка по name
                    </Typography>
                    <Typography
                        variant="body2"
                        className={styles.cardDescription}
                    >
                        Сгруппировать LabWork по имени, показать количество в
                        каждой группе.
                    </Typography>
                    <Button
                        variant="contained"
                        onClick={handleShowGrouped}
                        disabled={groupedLoading}
                        className={styles.cardButton}
                    >
                        Показать
                    </Button>
                    {groupedError && (
                        <Alert severity="error">{groupedError}</Alert>
                    )}
                    {groupedResult && (
                        <TableContainer>
                            <Table size="small" className={styles.table}>
                                <TableHead>
                                    <TableRow>
                                        <TableCell>Name</TableCell>
                                        <TableCell align="right">
                                            Count
                                        </TableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {groupedResult.map((row) => (
                                        <TableRow key={row.name}>
                                            <TableCell>{row.name}</TableCell>
                                            <TableCell align="right">
                                                {row.count}
                                            </TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        </TableContainer>
                    )}
                </Paper>

                <Paper className={styles.card}>
                    <Typography className={styles.cardTitle}>
                        minimalPoint больше заданного
                    </Typography>
                    <Typography
                        variant="body2"
                        className={styles.cardDescription}
                    >
                        Список LabWork, у которых minimalPoint больше порога.
                    </Typography>
                    <div className={styles.row}>
                        <TextField
                            label="Порог"
                            value={threshold}
                            onChange={(e) => setThreshold(e.target.value)}
                        />
                        <Button
                            variant="contained"
                            onClick={handleFindByMinPoint}
                            disabled={minPointLoading}
                        >
                            Найти
                        </Button>
                    </div>
                    {minPointError && (
                        <Alert severity="error">{minPointError}</Alert>
                    )}
                    {minPointResult && (
                        <div className={styles.result}>
                            <div className={styles.resultText}>
                                {minPointResult.length === 0
                                    ? 'Ничего не найдено'
                                    : minPointResult
                                          .map(
                                              (lw) =>
                                                  `${lw.name} (${lw.minimalPoint})`,
                                          )
                                          .join(', ')}
                            </div>
                        </div>
                    )}
                </Paper>

                <Paper className={styles.card}>
                    <Typography className={styles.cardTitle}>
                        Удалить LabWork из программы дисциплины
                    </Typography>
                    <Typography
                        variant="body2"
                        className={styles.cardDescription}
                    >
                        discipline у LabWork обязательно — эквивалентно удалению
                        самой LabWork.
                    </Typography>
                    <div className={styles.row}>
                        <TextField
                            className={styles.field}
                            label="LabWork"
                            select
                            value={deleteSelectedId}
                            onChange={(e) =>
                                setDeleteSelectedId(e.target.value)
                            }
                        >
                            {labWorks.map((lw) => (
                                <MenuItem key={lw.id} value={String(lw.id)}>
                                    {lw.name} (id {lw.id})
                                </MenuItem>
                            ))}
                        </TextField>
                        <Button
                            variant="contained"
                            disabled={!deleteSelectedId}
                            onClick={() => setDeleteConfirmOpen(true)}
                        >
                            Удалить
                        </Button>
                    </div>
                    {deleteError && (
                        <Alert severity="error">{deleteError}</Alert>
                    )}
                    {deleteDone && (
                        <Alert severity="success">{deleteDone}</Alert>
                    )}
                </Paper>

                <Paper className={`${styles.card} ${styles.cardWide}`}>
                    <Typography className={styles.cardTitle}>
                        Добавить 3 самых сложных LabWork в дисциплину
                    </Typography>
                    <Typography
                        variant="body2"
                        className={styles.cardDescription}
                    >
                        Переназначает 3 LabWork с наибольшей сложностью в
                        выбранную дисциплину.
                    </Typography>
                    <div className={styles.row}>
                        <TextField
                            className={styles.field}
                            label="Дисциплина"
                            select
                            value={targetDisciplineId}
                            onChange={(e) =>
                                setTargetDisciplineId(e.target.value)
                            }
                        >
                            {disciplines.map((d) => (
                                <MenuItem key={d.id} value={String(d.id)}>
                                    {d.name}
                                </MenuItem>
                            ))}
                        </TextField>
                        <Button
                            variant="contained"
                            disabled={!targetDisciplineId || addLoading}
                            onClick={handleAddHardest}
                        >
                            Добавить
                        </Button>
                    </div>
                    {addError && <Alert severity="error">{addError}</Alert>}
                    {addResult && (
                        <div className={styles.result}>
                            <div className={styles.resultText}>
                                {addResult.length === 0
                                    ? 'Нечего добавлять — самые сложные уже в этой дисциплине'
                                    : `Добавлено: ${addResult
                                          .map((lw) => lw.name)
                                          .join(', ')}`}
                            </div>
                        </div>
                    )}
                </Paper>
            </div>

            <ConfirmDialog
                open={deleteConfirmOpen}
                title="Удалить LabWork?"
                message={`Удалить "${deleteTarget?.name}"? Это действие необратимо.`}
                onConfirm={handleConfirmDelete}
                onCancel={() => setDeleteConfirmOpen(false)}
            />
        </div>
    );
}

export default SpecialOperationsPage;
