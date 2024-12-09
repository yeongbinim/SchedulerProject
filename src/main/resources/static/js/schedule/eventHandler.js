import {
  renderAllSchedules,
  renderCreateScheduleForm,
  renderEditScheduleForm,
  renderViewSchedule
} from './render.js';
import {
  createSchedule,
  deleteSchedule,
  getScheduleById,
  getSchedules,
  updateSchedule
} from './api.js';

export function init($main) {
  const $target = document.createElement("div");
  $target.setAttribute("id", "schedule");
  $target.addEventListener('click', async (event) => {
    const action = event.target.dataset.action;
    const scheduleId = event.target.closest('.schedule-item')?.dataset.id;

    if (action === 'view' && scheduleId) {
      await viewSchedule(scheduleId);
    } else if (action === 'edit' && scheduleId) {
      await editScheduleForm(scheduleId);
    } else if (action === 'delete' && scheduleId) {
      await deleteScheduleHandler(scheduleId);
    } else if (action === 'create') {
      await createScheduleForm();
    } else if (action === 'back') {
      await loadAllSchedules();
    } else if (action.startsWith('page-')) {
      const pageNumber = parseInt(action.split('-')[1]);
      await loadAllSchedules(pageNumber);
    }
  });
  $main.appendChild($target);
  loadAllSchedules();
}

async function loadAllSchedules(page = 0) {
  const schedules = await getSchedules(page);
  renderAllSchedules(schedules);
}

async function viewSchedule(id) {
  const schedule = await getScheduleById(id);
  renderViewSchedule(schedule);
}

async function createScheduleForm() {
  renderCreateScheduleForm();
  document.getElementById('createScheduleForm').addEventListener('submit',
      async (event) => {
        event.preventDefault();
        const content = document.getElementById('content').value;
        const memberId = document.getElementById('memberId').value;
        await createSchedule(memberId, {content});
        await loadAllSchedules();
      });
}

async function editScheduleForm(id) {
  const schedule = await getScheduleById(id);
  renderEditScheduleForm(schedule);

  document.getElementById('editScheduleForm').addEventListener('submit',
      async (event) => {
        event.preventDefault();
        const content = document.getElementById('content').value;
        const password = document.getElementById('password').value;
        await updateSchedule(id, {content, password});
        await loadAllSchedules();
      });
}

async function deleteScheduleHandler(id) {
  const password = prompt('지우기 위한 비밀번호를 입력해주세요: ');
  if (!password) {
    return;
  }
  await deleteSchedule(id, {password});
  await loadAllSchedules();
}

