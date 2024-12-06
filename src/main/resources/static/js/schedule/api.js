export async function getSchedules() {
  const response = await fetch('/api/schedules', {
    method: 'GET'
  });
  if (!response.ok) {
    throw new Error('Failed to fetch schedules');
  }
  return response.json();
}

export async function getScheduleById(id) {
  const response = await fetch(`/api/schedules/${id}`, {
    method: 'GET'
  });
  if (!response.ok) {
    throw new Error(`Failed to fetch schedule with ID ${id}`);
  }
  return response.json();
}

export async function createSchedule(memberId, scheduleData) {
  const url = `/api/schedules?memberId=${encodeURIComponent(memberId)}`;
  const response = await fetch(url, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(scheduleData)
  });
  if (!response.ok) {
    throw new Error('Failed to create schedule');
  }
}

export async function updateSchedule(id, scheduleData) {
  const response = await fetch(`/api/schedules/${id}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(scheduleData)
  });
  if (!response.ok) {
    throw new Error(`Failed to update schedule with ID ${id}`);
  }
  return response.json();
}

export async function deleteSchedule(id, scheduleData) {
  const response = await fetch(`/api/schedules/${id}`, {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(scheduleData)
  });
  if (!response.ok) {
    throw new Error(`Failed to delete schedule with ID ${id}`);
  }
  return response;
}
