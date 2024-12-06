const TARGET_ID = "schedule";

export function renderAllSchedules(schedules) {
  const $target = document.getElementById(TARGET_ID);
  $target.innerHTML = `
        <h1>All Schedules</h1>
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Content</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
          ${schedules.map(schedule =>
      `<tr class="schedule-item" data-id="${schedule.id}">
                <td>${schedule.id}</td>
                <td>${schedule.content}</td>
                <td>
                  <button data-action="view">View</button>
                  <button data-action="edit">Edit</button>
                  <button data-action="delete">Delete</button>
                </td>
              </tr>`).join('')}
          </tbody>
        </table>
      <br/>
      <button data-action="create">Create New Schedule</button>
  `;
}

export function renderViewSchedule(schedule) {
  const $target = document.getElementById(TARGET_ID);
  $target.innerHTML = `
        <h1>View Schedule</h1>
        <div class="schedule-item" data-id="${schedule.id}">
            <p>ID: ${schedule.id}</p>
            <p>Content: ${schedule.content}</p>
        </div>
        <br/>
        <button data-action="back">Back to list</button>`;
}

export function renderCreateScheduleForm() {
  const $target = document.getElementById(TARGET_ID);
  $target.innerHTML = `<h1>Create Schedule</h1>
        <form id="createScheduleForm">
            <label for="content">Content:</label>
            <input type="text" id="content" name="content" required>
            <label for="memberId">회원 아이디:</label>
            <input type="text" id="memberId" name="memberId" required>
            <button type="submit">Create</button>
        </form>
        <br/>
        <button data-action="back">Back to list</button>
`;
}

export function renderEditScheduleForm(schedule) {
  const $target = document.getElementById(TARGET_ID);
  $target.innerHTML = `<h1>Edit Schedule</h1>
        <form id="editScheduleForm">
            <label for="content">Content:</label>
            <input type="text" id="content" name="content" value="${schedule.content}" required>
            <label for="password">비밀번호:</label>
            <input type="password" id="password" name="password" required>
            <button type="submit">Update</button>
        </form>
        <br/>
        <button data-action="back">Back to list</button>
`;
}
